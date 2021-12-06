package net.celloscope.reactivespringdemo.book.application.port.out;

import net.celloscope.reactivespringdemo.book.application.port.in.SaveReviewCommand;
import net.celloscope.reactivespringdemo.book.domain.Review;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReviewCRUDPort {
  Flux<Review> loadAllReviewByBookId(Long bookId);
  Mono<Review> saveReview(Mono<SaveReviewCommand> command);
  Mono<Review> updateReview(Mono<SaveReviewCommand> command,Long reviewId);
  Mono<Void> deleteReviewByBookId(Long bookId);
  Mono<Void> deleteReviewByReviewId(Long reviewId);
}
