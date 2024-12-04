package com.api.socialbookbackend.book;

import com.api.socialbookbackend.common.PageResponse;
import com.api.socialbookbackend.handler.OperationNotPermittedException;
import com.api.socialbookbackend.history.BookTransactionHistory;
import com.api.socialbookbackend.history.BookTransactionHistoryRepository;
import com.api.socialbookbackend.user.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.api.socialbookbackend.book.BookMapper.toBook;
import static com.api.socialbookbackend.book.BookSpecification.withOwnerId;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookTransactionHistoryRepository bookTransactionHistoryRepository;
    private final FileStorageService fileStorageService;


    public Long saveBook(@Valid BookRequest bookRequest, Authentication connectedUser) {
        User retrieveConnectedUser = (User) connectedUser.getPrincipal();
        Book newBook = toBook(bookRequest);
        newBook.setOwner(retrieveConnectedUser);
        return bookRepository.save(newBook).getId();
    }

    public BookResponse findBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .map(BookMapper::toBookResponse)
                .orElseThrow(() -> new EntityNotFoundException("Book not found for id: " + bookId));
    }

    public PageResponse<BookResponse> findAllBooksNotOwnedByConnectedUser(int page, int size, Authentication connectedUser) {
        User retrieveConnectedUser = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());

        Page<Book> books = bookRepository.findAllBooksNotOwnedByConnectedUser(retrieveConnectedUser.getId(), pageable);
        List<BookResponse> bookResponses = books.stream()
                .map((BookMapper::toBookResponse))
                .toList();

        return new PageResponse<>(
                bookResponses,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isLast(),
                books.isFirst());
    }

    public PageResponse<BookResponse> findAllBooksOwnedByConnectedUser(int page, int size, Authentication connectedUser) {
        User retrieveConnectedUser = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdDate").descending());

        Page<Book> books = bookRepository.findAllBooksOwnedByConnectedUser(withOwnerId(retrieveConnectedUser.getId()), pageable);

        List<BookResponse> bookResponses = books.stream()
                .map(BookMapper::toBookResponse)
                .toList();

        return PageResponse.<BookResponse>builder()
                .totalPages(books.getTotalPages())
                .totalElements(books.getTotalElements())
                .content(bookResponses)
                .first(books.isFirst())
                .last(books.isLast())
                .number(books.getNumber())
                .size(books.getSize())
                .build();
    }

    public PageResponse<BorrowedBookResponse> findAllBorrowedBooksByConnectedUser(int page, int size, Authentication connectedUser) {
        User retrieveConnectedUser = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdDate").descending());
        Page<BookTransactionHistory> AllBorrowedBooks = bookTransactionHistoryRepository.findBooksBorrowedByUser(retrieveConnectedUser.getId(), pageable);

        List<BorrowedBookResponse> bookResponses = AllBorrowedBooks.stream()
                .map(BookMapper::toBorrowedBookResponse)
                .toList();

        return PageResponse.<BorrowedBookResponse>builder()
                .number(AllBorrowedBooks.getNumber())
                .size(AllBorrowedBooks.getSize())
                .totalElements(AllBorrowedBooks.getTotalElements())
                .first(AllBorrowedBooks.isFirst())
                .last(AllBorrowedBooks.isLast())
                .totalPages(AllBorrowedBooks.getTotalPages())
                .content(bookResponses)
                .build();

    }

    public PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size, Authentication connectedUser) {
        User retrieveConnectedUser = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdDate").descending());

        Page<BookTransactionHistory> AllReturnedBooks = bookTransactionHistoryRepository.findAllReturnedBooks(retrieveConnectedUser.getId(), pageable);
        List<BorrowedBookResponse> bookResponses = AllReturnedBooks.stream()
                .map(BookMapper::toBorrowedBookResponse)
                .toList();

        return PageResponse.<BorrowedBookResponse>builder()
                .number(AllReturnedBooks.getNumber())
                .size(AllReturnedBooks.getSize())
                .totalElements(AllReturnedBooks.getTotalElements())
                .first(AllReturnedBooks.isFirst())
                .last(AllReturnedBooks.isLast())
                .totalPages(AllReturnedBooks.getTotalPages())
                .content(bookResponses)
                .build();

    }

    public Long updateBookSharableStatus(Long bookId, Authentication connectedUser) {
        User retrieveConnectedUser = (User) connectedUser.getPrincipal();
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found for id: " + bookId));

        if (book.getOwner().getId().equals(retrieveConnectedUser.getId())) {
            book.setShareable(!book.isShareable());
            bookRepository.save(book);
            return book.getId();
        } else {
            throw new OperationNotPermittedException("You are not the owner of this book; Cannot change the sharable status");
        }
    }

    public Long updateArchiveStatus(Long bookId, Authentication connectedUser) {
        User retrieveConnectedUser = (User) connectedUser.getPrincipal();
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found for id: " + bookId));

        if (book.getOwner().getId().equals(retrieveConnectedUser.getId())) {
            book.setArchived(!book.isArchived());
            bookRepository.save(book);
            return book.getId();
        } else {
            throw new OperationNotPermittedException("You are not the owner of this book; Cannot archive this book");
        }
    }

    public Long borrowBook(Long bookId, Authentication connectedUser) {
        User retrieveConnectedUser = (User) connectedUser.getPrincipal();
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found for id: " + bookId));

        if(book.getOwner().getId().equals(retrieveConnectedUser.getId())){
            throw new OperationNotPermittedException("You cannot borrow your own book");
        }

        if(book.isArchived() || !book.isShareable()){
            throw new OperationNotPermittedException("This book is not available for borrowing");
        }

        final boolean isBookAlreadyBorrowed = bookTransactionHistoryRepository.isAlreadyBorrowedByUser(bookId, retrieveConnectedUser.getId());

        if(isBookAlreadyBorrowed){
            throw new OperationNotPermittedException("Requested book is already borrowed");
        }

        BookTransactionHistory bookTransactionHistory = BookTransactionHistory.builder()
                .book(book)
                .user(retrieveConnectedUser)
                .returned(false)
                .returnedApproved(false)
                .build();
        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    public Long returnBorrowedBook(Long bookId, Authentication connectedUser) {
        User retrieveConnectedUser = (User) connectedUser.getPrincipal();
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found for id: " + bookId));

        if(book.isArchived() || !book.isShareable()){
            throw new OperationNotPermittedException("This book is not available for borrowing");
        }
        if(book.getOwner().getId().equals(retrieveConnectedUser.getId())){
            throw new OperationNotPermittedException("You cannot return your own book");
        }
        BookTransactionHistory bookTransactionHistory = bookTransactionHistoryRepository.findByBookIdAndUserId(bookId, retrieveConnectedUser.getId())
                .orElseThrow(() -> new OperationNotPermittedException("You have not borrowed this book"));
        bookTransactionHistory.setReturned(true);
        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();

    }

    public Long approveReturnedBook(Long bookId, Authentication connectedUser) {
        User retrieveConnectedUser = (User) connectedUser.getPrincipal();
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found for id: " + bookId));

        if(book.isArchived() || !book.isShareable()){
            throw new OperationNotPermittedException("This book is not available for returning");
        }

        if(book.getOwner().getId().equals(retrieveConnectedUser.getId())){
            throw new OperationNotPermittedException("You cannot approve the return of your own book");
        }
        BookTransactionHistory bookTransactionHistory = bookTransactionHistoryRepository.findByBookIdAndOwnerId(bookId, retrieveConnectedUser.getId())
                .orElseThrow(() -> new OperationNotPermittedException("The book is not returned by the borrower so you cannot approve the return yet"));

        bookTransactionHistory.setReturnedApproved(true);
        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();

    }

    public void uploadBookCover(Long bookId, MultipartFile file, Authentication connectedUser) {
        User retrieveConnectedUser = (User) connectedUser.getPrincipal();
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found for id: " + bookId));
        var bookCover = fileStorageService.saveFile(file,retrieveConnectedUser.getId());
        book.setBookCover(bookCover);
        bookRepository.save(book);

    }
}
