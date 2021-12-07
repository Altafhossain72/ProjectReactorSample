package net.celloscope.reactivespringdemo.book.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookInfo {
    private Long bookId;
    private String title;
    private String author;
    private String ISBN;
}
