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

    @GetMapping("/not-owned-by-connected-user")
    public ResponseEntity<PageResponse<BookResponse>> findAllBooksNotOwnedByConnectedUser(
            @RequestParam(name = "page", defaultValue = "0",required = false) int page,
            @RequestParam(name = "size", defaultValue = "10",required = false) int size,
            Authentication connectedUser) {
        return ResponseEntity.ok(bookService.findAllBooksNotOwnedByConnectedUser(page, size, connectedUser));
    }

    @GetMapping("/owned-by-connected-user")
    public ResponseEntity<PageResponse<BookResponse>> findAllBooksOwnedByConnectedUser(
            @RequestParam(name = "page", defaultValue = "0",required = false) int page,
            @RequestParam(name = "size", defaultValue = "10",required = false) int size,
            Authentication connectedUser) {
        return ResponseEntity.ok(bookService.findAllBooksOwnedByConnectedUser(page, size, connectedUser));
    }

    @GetMapping("/borrowed-by-connected-user")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> findAllBorrowedBooks(
            @RequestParam(name = "page", defaultValue = "0",required = false) int page,
            @RequestParam(name = "size", defaultValue = "10",required = false) int size,
            Authentication connectedUser) {
        return ResponseEntity.ok(bookService.findAllBorrowedBooksByConnectedUser(page, size, connectedUser));
    }

    @GetMapping("/returned-books")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> findAllReturnedBooks(
            @RequestParam(name = "page", defaultValue = "0",required = false) int page,
            @RequestParam(name = "size", defaultValue = "10",required = false) int size,
            Authentication connectedUser) {
        return ResponseEntity.ok(bookService.findAllReturnedBooks(page, size, connectedUser));
    }

    @PatchMapping("/sharable/{book-id}")
    public ResponseEntity<Long> updateBookSharableStatus(@PathVariable("book-id") Long bookId, Authentication connectedUser) {
        return ResponseEntity.ok(bookService.updateBookSharableStatus(bookId,connectedUser));
    }

    @PatchMapping("/archive/{book-id}")
    public ResponseEntity<Long> updateArchiveStatus(@PathVariable("book-id") Long bookId, Authentication connectedUser) {
        return ResponseEntity.ok(bookService.updateArchiveStatus(bookId,connectedUser));
    }

    @PostMapping("/borrow/{book-id}")
    public ResponseEntity<Long> borrowBook(@PathVariable("book-id") Long bookId, Authentication connectedUser) {
        return ResponseEntity.ok(bookService.borrowBook(bookId, connectedUser));
    }

    @PatchMapping("/borrow/return/{book-id}")
    public ResponseEntity<Long> returnBorrowedBook(@PathVariable("book-id") Long bookId, Authentication connectedUser) {
        return ResponseEntity.ok(bookService.returnBorrowedBook(bookId, connectedUser));
    }

    //5:24:48




}

