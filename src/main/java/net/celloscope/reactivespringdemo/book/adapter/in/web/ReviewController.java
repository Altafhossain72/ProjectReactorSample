package net.celloscope.reactivespringdemo.book.adapter.in.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.celloscope.reactivespringdemo.book.application.port.in.ReviewCRUDUseCase;
import net.celloscope.reactivespringdemo.book.application.port.in.SaveReviewCommand;
import net.celloscope.reactivespringdemo.book.domain.Review;
import net.celloscope.reactivespringdemo.common.ExceptionHandlerUtil;
import net.celloscope.reactivespringdemo.common.Messages;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@Validated
@Slf4j
@RequestMapping("/v1")
public class ReviewController {
    private final ReviewCRUDUseCase reviewCRUDUseCase;


    @GetMapping("/review/{bookId}")
    public Flux<Review> getAllReviewByBookId(
            @PathVariable("bookId") @NotBlank(message = "bookId  can not be null") @NotEmpty(message = "bookId  can not be empty") Long bookId) {
        return this.reviewCRUDUseCase.loadReviewByBookId(bookId)
                .switchIfEmpty(Mono.error(new ExceptionHandlerUtil(HttpStatus.NOT_FOUND, Messages.NOT_FOUND)))
                .onErrorMap(
                        throwable -> {
                            log.error("Exception Occurred while loadReviewByBookId :" + throwable);
                            return new ResponseStatusException(HttpStatus.NOT_FOUND, throwable.getMessage(), throwable.getCause());
                        }
                );
    }

    @PostMapping("/create-review")
    public Mono<ResponseEntity<Review>> createReviewForBook(@RequestBody Mono<SaveReviewCommand> command) {
        return reviewCRUDUseCase.saveReview(command)
                .map(ResponseEntity::ok)
                .onErrorMap(
                        throwable -> {
                            log.error("Exception Occurred while saveReview :" + throwable);
                            return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, throwable.getMessage(), throwable.getCause());
                        }
                );
    }

    @PutMapping("/update-review/{reviewId}")
    public Mono<ResponseEntity<Review>> updateSingleReview(
            @PathVariable("reviewId") @NotBlank(message = "reviewId  can not be null") @NotEmpty(message = "reviewId  can not be empty") Long reviewId,
            @RequestBody Mono<SaveReviewCommand> command) {
        return this.reviewCRUDUseCase.updateReview(command, reviewId)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new ExceptionHandlerUtil(HttpStatus.INTERNAL_SERVER_ERROR, Messages.INTERNAL_SERVER_ERROR)))
                .onErrorMap(
                        throwable -> {
                            log.error("Exception Occurred while updateReview :" + throwable);
                            return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, throwable.getMessage(), throwable.getCause());
                        }
                );
    }

    @DeleteMapping("/delete-all-review/{bookId}")
    public Mono<ResponseEntity<Void>> deleteAllReviewForBook(@PathVariable("bookId") @NotBlank(message = "bookId  can not be null") @NotEmpty(message = "bookId  can not be empty") Long bookId) {
        return this.reviewCRUDUseCase.deleteAllReviewForSingleBook(bookId)
                .map(ResponseEntity::ok)
                .onErrorMap(
                        throwable -> {
                            log.error("Exception Occurred while deleteAllReviewForSingleBook :" + throwable);
                            return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, throwable.getMessage(), throwable.getCause());
                        }
                );
    }

    @DeleteMapping("/delete-single-review/{reviewId}")
    public Mono<ResponseEntity<Void>> deleteSingleReviewForBook(  @PathVariable("reviewId") @NotBlank(message = "reviewId  can not be null") @NotEmpty(message = "reviewId  can not be empty") Long reviewId) {
        return this.reviewCRUDUseCase.deleteSingleReviewForSingleBook(reviewId)
                .map(ResponseEntity::ok)
                .onErrorMap(
                        throwable -> {
                            log.error("Exception Occurred while deleteSingleReviewForSingleBook :" + throwable);
                            return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, throwable.getMessage(), throwable.getCause());
                        }
                );
    }
}
