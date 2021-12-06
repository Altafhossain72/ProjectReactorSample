package net.celloscope.reactivespringdemo.book.adapter.out.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("books")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookInfoDBEntity {
    @Id
    private Long bookId;
    private String title;
    private String author;
    private String ISBN;
}
