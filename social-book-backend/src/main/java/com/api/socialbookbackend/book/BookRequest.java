package com.api.socialbookbackend.book;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record BookRequest(

        @NotNull(message = "100") @NotEmpty(message = "100") String title,

        @NotNull(message = "101") @NotEmpty(message = "101") String authorName,

        @NotNull(message = "102") @NotEmpty(message = "102") String synopsis,
        @NotNull(message = "103") @NotEmpty(message = "103") String isbn,
        @NotNull(message = "104") @NotEmpty(message = "104") String bookCover,

        boolean shareable //optional
) {
}
