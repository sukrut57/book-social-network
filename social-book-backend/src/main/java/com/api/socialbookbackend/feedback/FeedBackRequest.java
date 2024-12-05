package com.api.socialbookbackend.feedback;

import jakarta.validation.constraints.*;

public record FeedBackRequest(

        @Positive(message = "200")
        @Min(value = 1, message = "200")
        @Max(value = 5, message = "200")
        Double note,

        @NotNull(message = "201")
        @NotEmpty(message = "201")
        @NotBlank(message = "201")
        String comment,

        @NotNull(message = "202")
        Long bookId

) {
}

