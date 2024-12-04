package com.api.socialbookbackend.feedback;

import jakarta.validation.constraints.*;

public record FeedBackRequest(

        @Positive(message = "200")
        @Size(min = 1, max = 5, message = "note must be between 1 and 5")
        Double note,

        @NotNull(message = "201")
        @NotEmpty(message = "201")
        @NotBlank(message = "201")
        String comment,

        @NotNull(message = "202")
        Long bookId

) {
}

