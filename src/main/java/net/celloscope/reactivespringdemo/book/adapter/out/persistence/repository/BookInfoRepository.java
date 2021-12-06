package net.celloscope.reactivespringdemo.book.adapter.out.persistence.repository;

import net.celloscope.reactivespringdemo.book.adapter.out.persistence.entity.BookInfoDBEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface BookInfoRepository extends ReactiveCrudRepository<BookInfoDBEntity,Long> {
    Mono<BookInfoDBEntity> findByBookId(Long bookId);
}
