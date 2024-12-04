package com.api.socialbookbackend.book;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class BookSpecification {

    /**
     * Returns a specification that filters books by their owner's id.
     *
     * @param ownerId the id of the owner to filter by
     * @return the specification
     */
    public static Specification<Book> withOwnerId(Long ownerId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("owner").get("id"), ownerId);
    }

}
