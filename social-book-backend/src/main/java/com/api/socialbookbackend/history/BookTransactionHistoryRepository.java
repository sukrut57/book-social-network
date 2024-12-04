package com.api.socialbookbackend.history;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookTransactionHistoryRepository extends JpaRepository<BookTransactionHistory,Long> {


    @Query("""
            SELECT bookTransactionHistory
            FROM BookTransactionHistory bookTransactionHistory
            WHERE bookTransactionHistory.user.id = :userId
            """)
    Page<BookTransactionHistory> findBooksBorrowedByUser(Long userId, Pageable pageable);

    @Query("""
            SELECT bookTransactionHistory
            FROM BookTransactionHistory bookTransactionHistory
            WHERE bookTransactionHistory.book.owner.id = :userId
            """)
    Page<BookTransactionHistory> findAllReturnedBooks(Long userId, Pageable pageable);

    @Query("""
            SELECT
            (COUNT(*) > 0) AS isBorrowed
            FROM BookTransactionHistory bookTransactionHistory
            WHERE bookTransactionHistory.user.id = :userId
            AND bookTransactionHistory.book.id = :bookId
            AND bookTransactionHistory.returnedApproved = false
            """)
    boolean isAlreadyBorrowedByUser(Long bookId, Long userId);

    @Query("""
            SELECT bookTransactionHistory
            FROM BookTransactionHistory bookTransactionHistory
            WHERE bookTransactionHistory.user.id = :userId
            AND bookTransactionHistory.book.id = :bookId
            AND bookTransactionHistory.returned = false
            AND bookTransactionHistory.returnedApproved = false
            """)
    Optional<BookTransactionHistory> findByBookIdAndUserId(Long bookId, Long userId);

    @Query("""
           SELECT bookTransactionHistory
           FROM BookTransactionHistory bookTransactionHistory
           WHERE bookTransactionHistory.book.id = :bookId
           AND bookTransactionHistory.book.owner.id = :userId
           AND bookTransactionHistory.returned = true
           AND bookTransactionHistory.returnedApproved = false
           """)
    Optional<BookTransactionHistory> findByBookIdAndOwnerId(Long bookId, Long userId);
}
