package com.api.socialbookbackend.feedback;

import com.api.socialbookbackend.book.Book;
import com.api.socialbookbackend.shared.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Feedback extends BaseEntity {

    private Double note; //1 to 5 stars ratings
    private String comment;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;



}
