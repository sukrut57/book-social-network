package com.api.socialbookbackend.book;

import com.api.socialbookbackend.history.BookTransactionHistory;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {
    public static Book toBook(@Valid BookRequest bookRequest) {
        return Book.builder()
                .title(bookRequest.title())
                .authorName(bookRequest.authorName())
                .synopsis(bookRequest.synopsis())
                .isbn(bookRequest.isbn())
                .bookCover(bookRequest.bookCover())
                .shareable(bookRequest.shareable())
                .archived(false)
                .build();
    }
    public static BookResponse toBookResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .authorName(book.getAuthorName())
                .synopsis(book.getSynopsis())
                .isbn(book.getIsbn())
                .shareable(book.isShareable())
                .archived(book.isArchived())
                .owner(book.getOwner().getFullName())
                .bookCover(FileUtils.readFromFileLocation(book.getBookCover()))
                .rate(book.getRate())
                .build();
    }

    public static BorrowedBookResponse toBorrowedBookResponse(BookTransactionHistory bookTransactionHistory) {
        return BorrowedBookResponse.builder()
                .id(bookTransactionHistory.getId())
                .returned(bookTransactionHistory.isReturned())
                .returnedApproved(bookTransactionHistory.isReturnedApproved())
                .title(bookTransactionHistory.getBook().getTitle())
                .authorName(bookTransactionHistory.getBook().getAuthorName())
                .rate(bookTransactionHistory.getBook().getRate())
                .isbn(bookTransactionHistory.getBook().getIsbn())
                .bookId(bookTransactionHistory.getBook().getId())
                .build();
    }
}
