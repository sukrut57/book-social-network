package com.api.socialbookbackend.book;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponse {
    private Long id;
    private String title;
    private String authorName;
    private String synopsis;
    private String isbn;
    private boolean shareable;
    private boolean archived;
    private String owner;
    private double rate;

   // private byte[] bookCover;


}
