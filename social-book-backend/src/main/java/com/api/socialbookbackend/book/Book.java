package com.api.socialbookbackend.book;

import com.api.socialbookbackend.feedback.Feedback;
import com.api.socialbookbackend.history.BookTransactionHistory;
import com.api.socialbookbackend.shared.BaseEntity;
import com.api.socialbookbackend.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;


@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Book extends BaseEntity {

    private String title;
    private String authorName;
    private String synopsis;
    private String isbn;
    private String bookCover;
    private boolean archived;
    private boolean shareable;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "book")
    List<Feedback> feedbacks;

    @OneToMany(mappedBy = "book")
    List<BookTransactionHistory> bookTransactionHistories;

    @Transient
    public double getRate(){
        var rate = feedbacks.stream()
                .mapToDouble(Feedback::getNote)
                .average()
                .orElse(0.0);
        return Math.round(rate * 10.0) / 10.0;
    }


}
