package com.api.socialbookbackend.book;

import com.api.socialbookbackend.common.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("books")
@RequiredArgsConstructor
@Tag(name = "Book", description = "Book API")
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<Long> saveBook(@Valid @RequestBody BookRequest bookRequest, Authentication connectedUser) {
        return ResponseEntity.ok(bookService.saveBook(bookRequest, connectedUser));
    }


    @GetMapping("{book-id}")
    public ResponseEntity<BookResponse> findBookById(@PathVariable("book-id") Long bookId) {
        return ResponseEntity.ok(bookService.findBookById(bookId));
    }

    @GetMapping
    public ResponseEntity<PageResponse<BookResponse>> findAllBooksNotOwnedByConnectedUser(
            @RequestParam(name = "page", defaultValue = "0",required = false) int page,
            @RequestParam(name = "size", defaultValue = "10",required = false) int size,
            Authentication connectedUser) {
        return ResponseEntity.ok(bookService.findAllBooksNotOwnedByConnectedUser(page, size, connectedUser));
    }

}

