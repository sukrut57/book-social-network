package com.api.socialbookbackend.book;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BorrowedBookResponse {
    private Long id;
    private String title;
    private String authorName;
    private String isbn;
    private boolean returned;
    private boolean returnedApproved;
    private double rate;
}
