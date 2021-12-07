package net.celloscope.reactivespringdemo.book.application.port.in;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveReviewCommand {
    private Long reviewId;
    private Long bookId;
    private String comment;
}
