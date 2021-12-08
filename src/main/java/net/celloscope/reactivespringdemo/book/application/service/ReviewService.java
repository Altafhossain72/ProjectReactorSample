package net.celloscope.reactivespringdemo.book.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.celloscope.reactivespringdemo.book.application.port.in.ReviewCRUDUseCase;
import net.celloscope.reactivespringdemo.book.application.port.in.SaveReviewCommand;
import net.celloscope.reactivespringdemo.book.application.port.out.ReviewCRUDPort;
import net.celloscope.reactivespringdemo.book.domain.Review;
import net.celloscope.reactivespringdemo.common.ExceptionHandlerUtil;
import net.celloscope.reactivespringdemo.common.Messages;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
@Slf4j
public class ReviewService implements ReviewCRUDUseCase {

    private final ReviewCRUDPort reviewCRUDPort;

    @Override
    public Flux<Review> loadReviewByBookId(Long bookId) {
        return reviewCRUDPort.loadAllReviewByBookId(bookId)

                .onErrorMap(throwable -> {
                    log.error("Exception Occurred while loadReviewByBookId :" + throwable);
                    return new ExceptionHandlerUtil(HttpStatus.NOT_FOUND, Messages.NOT_FOUND);
                }).log();
    }

    @Override
    public Mono<Review> saveReview(Mono<SaveReviewCommand> command) {
       return reviewCRUDPort.saveReview(command)

               .onErrorMap(throwable -> {
                   log.error("Exception Occurred while saveReview:" + throwable);
                   return new ExceptionHandlerUtil(HttpStatus.INTERNAL_SERVER_ERROR, Messages.INTERNAL_SERVER_ERROR);
               }).log();
    }

    @Override
    public Mono<Review> updateReview(Mono<SaveReviewCommand> command, Long reviewId) {
        return reviewCRUDPort.updateReview(command,reviewId)
                .onErrorMap(throwable -> {
            log.error("Exception Occurred while updateReview :" + throwable);
            return new ExceptionHandlerUtil(HttpStatus.INTERNAL_SERVER_ERROR, Messages.INTERNAL_SERVER_ERROR);
        }).log();
    }

    @Override
    public Mono<Void> deleteAllReviewForSingleBook(Long bookId) {
         return reviewCRUDPort.deleteReviewByBookId(bookId)
                    .onErrorMap(throwable -> {
                        log.error("Exception Occurred while deleteReviewByBookId :" + throwable);
                        return new ExceptionHandlerUtil(HttpStatus.INTERNAL_SERVER_ERROR, Messages.INTERNAL_SERVER_ERROR);
                    }).log();
    }

    @Override
    public Mono<Void> deleteSingleReviewForSingleBook(Long reviewId) {
        return reviewCRUDPort.deleteReviewByReviewId(reviewId)
                .onErrorMap(throwable -> {
                    log.error("Exception Occurred while deleteReviewByReviewId :" + throwable);
                    return new ExceptionHandlerUtil(HttpStatus.INTERNAL_SERVER_ERROR, Messages.INTERNAL_SERVER_ERROR);
                }).log();
    }
}
