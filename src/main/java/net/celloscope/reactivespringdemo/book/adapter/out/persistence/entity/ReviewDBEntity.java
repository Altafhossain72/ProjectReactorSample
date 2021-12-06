package net.celloscope.reactivespringdemo.book.adapter.out.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("reviews")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDBEntity {
    @Id
    private Long reviewId;
    private Long bookId;
    private String comment;
}
