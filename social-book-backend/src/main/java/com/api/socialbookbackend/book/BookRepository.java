package com.api.socialbookbackend.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    @Query("""
            SELECT book
            FROM Book book
            WHERE book.owner.id != :id
            AND book.archived = false
            AND book.shareable = true

        """)
    Page<Book> findAllBooksNotOwnedByConnectedUser(Long id, Pageable pageable);


    Page<Book> findAllBooksOwnedByConnectedUser(Specification<Book> bookSpecification, Pageable pageable);
}
