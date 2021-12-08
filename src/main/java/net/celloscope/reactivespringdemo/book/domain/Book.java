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
public class Book {
    BookInfo bookInfo;
    List<Review> review;
}
