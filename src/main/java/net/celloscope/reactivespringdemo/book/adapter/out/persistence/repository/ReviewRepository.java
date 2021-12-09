package net.celloscope.reactivespringdemo.book.adapter.out.persistence.repository;


import net.celloscope.reactivespringdemo.book.adapter.out.persistence.entity.ReviewDBEntity;
import net.celloscope.reactivespringdemo.book.domain.Review;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ReviewRepository extends ReactiveCrudRepository<ReviewDBEntity,Long> {
    Flux<ReviewDBEntity> findByBookId(Long bookId);
    Mono<ReviewDBEntity> findByReviewId(Long reviewId);
    Mono<Void> deleteByBookId(Long bookId);
}
