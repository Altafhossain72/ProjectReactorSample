package net.celloscope.reactivespringdemo.book.application.port.in;

import net.celloscope.reactivespringdemo.book.domain.Review;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReviewCRUDUseCase {
    Flux<Review> loadReviewByBookId(Long bookId);
    Mono<Review> saveReview(Mono<SaveReviewCommand> command);
    Mono<Review> updateReview(Mono<SaveReviewCommand> command, Long reviewId);
    Mono<Void> deleteAllReviewForSingleBook(Long bookId);
    Mono<Void> deleteSingleReviewForSingleBook(Long bookId);
}
