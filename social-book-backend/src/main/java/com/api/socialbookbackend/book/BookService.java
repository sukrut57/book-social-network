package com.api.socialbookbackend.book;

import com.api.socialbookbackend.common.PageResponse;
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

import java.util.List;

import static com.api.socialbookbackend.book.BookMapper.toBook;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;


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
}
