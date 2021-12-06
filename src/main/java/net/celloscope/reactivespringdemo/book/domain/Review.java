package net.celloscope.reactivespringdemo.book.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    private Long reviewId;
    private Long bookId;
    private String comment;
}
