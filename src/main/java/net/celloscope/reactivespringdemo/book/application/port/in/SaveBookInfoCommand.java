package net.celloscope.reactivespringdemo.book.application.port.in;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveBookInfoCommand {
    private Long bookId;
    private String title;
    private String author;
    private String isbn;
}
