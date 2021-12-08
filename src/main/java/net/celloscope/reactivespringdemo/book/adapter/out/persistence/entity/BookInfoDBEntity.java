package net.celloscope.reactivespringdemo.book.adapter.out.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("books")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookInfoDBEntity {
    @Id
    @Column("bookid")
    private Long bookId;
    private String title;
    private String author;
    @Column("isbn")
    private String ISBN;
}
