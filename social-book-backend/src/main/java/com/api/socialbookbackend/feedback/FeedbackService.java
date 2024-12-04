package com.api.socialbookbackend.feedback;

import com.api.socialbookbackend.book.Book;
import com.api.socialbookbackend.book.BookRepository;
import com.api.socialbookbackend.common.PageResponse;
import com.api.socialbookbackend.handler.OperationNotPermittedException;
import com.api.socialbookbackend.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.api.socialbookbackend.feedback.FeedbackMapper.toFeedback;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final BookRepository bookRepository;

    public Long createFeedback(FeedBackRequest feedBackRequest, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();

        Book book = bookRepository.findById(feedBackRequest.bookId()).orElseThrow(
                () -> new EntityNotFoundException("Book not found with id: " + user.getId())
        );

        if(book.isArchived() || !book.isShareable()){
            throw new OperationNotPermittedException("You can't give feedback to this book");
        }

        if(user.getId().equals(book.getOwner().getId())){
            throw new OperationNotPermittedException("You can't give feedback to your own book");
        }

        Feedback feedback = toFeedback(feedBackRequest, book);
        return feedbackRepository.save(feedback).getId();
    }

    public PageResponse<FeedbackResponse> getFeedbacksByBookId(Long bookId, int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdDate").descending());

        Page<Feedback> feedbacks = feedbackRepository.findAllByBookId(bookId, pageable);

        List<FeedbackResponse> feedbackResponses = feedbacks.stream()
                .map(feedback -> FeedbackMapper.toFeedbackResponse(feedback, user))
                .toList();

        return PageResponse.<FeedbackResponse>builder()
                .number(feedbacks.getNumber())
                .size(feedbacks.getSize())
                .totalElements(feedbacks.getTotalElements())
                .first(feedbacks.isFirst())
                .last(feedbacks.isLast())
                .totalPages(feedbacks.getTotalPages())
                .content(feedbackResponses)
                .build();
    }
}
