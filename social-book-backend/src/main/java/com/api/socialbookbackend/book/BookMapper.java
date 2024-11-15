package com.api.socialbookbackend.book;

import jakarta.validation.Valid;
import org.hibernate.annotations.Comment;
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
                //TODO implement later
                //.cover(book.getBookCover())
                .rate(book.getRate())
                .build();
    }
}
