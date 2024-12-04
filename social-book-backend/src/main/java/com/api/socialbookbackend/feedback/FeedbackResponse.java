package com.api.socialbookbackend.feedback;

public record FeedbackResponse(
        Double note,
        String comment,
        boolean ownFeedback

) {
}
