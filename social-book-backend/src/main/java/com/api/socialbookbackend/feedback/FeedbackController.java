package com.api.socialbookbackend.feedback;

import com.api.socialbookbackend.common.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
@Tag(name = "Feedback", description = "Feedback API")
public class FeedbackController {
    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<Long> saveFeedBack(@Valid @RequestBody FeedBackRequest feedBackRequest,
                                                Authentication connectedUser) {
        return ResponseEntity.ok(feedbackService.createFeedback(feedBackRequest,connectedUser));
    }

    @GetMapping("/book/{book-id}")
    public ResponseEntity<PageResponse<FeedbackResponse>> getFeedbacksByBookId(@PathVariable("book-id") Long bookId,
                                                                               @RequestParam(value = "page", defaultValue = "0") int page,
                                                                               @RequestParam(value = "size", defaultValue = "10") int size,
                                                                               Authentication connectedUser) {
        return ResponseEntity.ok(feedbackService.getFeedbacksByBookId(bookId, page, size,connectedUser));
    }

}
