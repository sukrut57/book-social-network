package com.api.socialbookbackend.feedback;

import com.api.socialbookbackend.book.Book;
import com.api.socialbookbackend.user.User;
import org.springframework.stereotype.Component;

@Component
public class FeedbackMapper {

    public static Feedback toFeedback(FeedBackRequest feedbackRequest, Book book) {
        return Feedback.builder()
                .note(feedbackRequest.note())
                .comment(feedbackRequest.comment())
                .book(book)
                .build();
    }


    public static FeedbackResponse toFeedbackResponse(Feedback feedback,User user) {
        return new FeedbackResponse(
                feedback.getNote(),
                feedback.getComment(),
                feedback.getBook().getOwner().getId().equals(user.getId()),
                feedback.getCreatedBy()
        );
    }
}
