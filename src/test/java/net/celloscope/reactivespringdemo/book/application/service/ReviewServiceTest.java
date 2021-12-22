package net.celloscope.reactivespringdemo.book.application.service;

import net.celloscope.reactivespringdemo.book.application.port.in.SaveReviewCommand;
import net.celloscope.reactivespringdemo.book.application.port.out.ReviewCRUDPort;
import net.celloscope.reactivespringdemo.book.domain.Review;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    ReviewCRUDPort reviewCRUDPort;

    @InjectMocks
    ReviewService reviewService;

    @Test
    void loadReviewByBookId() {
        when(reviewCRUDPort.loadAllReviewByBookId(Mockito.anyLong())).thenReturn(buildMockFluxReview());
        Flux<Review> reviewFlux = reviewService.loadReviewByBookId(2l);
        StepVerifier.create(reviewFlux)
                .assertNext(
                        review -> compareTwoObjectRecursively(review,buildMockReview())
                ).assertNext(
                        review -> compareTwoObjectRecursively(review,buildMockReview())
                ).assertNext(
                        review -> compareTwoObjectRecursively(review,buildMockReview())
                ).verifyComplete();

    }


    @Test
    void saveReview() {
        when(reviewCRUDPort.saveReview(Mockito.any())).thenReturn(Mono.just(buildMockReview()));
        Mono<Review> reviewMono = reviewService.saveReview(buildMockMonoSaveReviewCommand());
        StepVerifier.create(reviewMono)
                .assertNext(
                        review -> compareTwoObjectRecursively(review,buildMockReview())
                )
                .verifyComplete();
    }



    @Test
    void updateReview() {
        when(reviewCRUDPort.updateReview(Mockito.any(),Mockito.anyLong())).thenReturn(Mono.just(buildMockReview()));
        Mono<Review> reviewMono = reviewService.updateReview(buildMockMonoSaveReviewCommand(),5l);
        StepVerifier.create(reviewMono)
                .assertNext(
                        review -> compareTwoObjectRecursively(review,buildMockReview())
                )
                .verifyComplete();
    }

    @Test
    void deleteAllReviewForSingleBook() {
        when(reviewCRUDPort.deleteReviewByBookId(Mockito.anyLong())).thenReturn(Mono.empty());
        Mono<Void> voidMono = reviewService.deleteAllReviewForSingleBook(5l);
        StepVerifier.create(voidMono)
                .verifyComplete();
    }

    @Test
    void deleteSingleReviewForSingleBook() {
        when(reviewCRUDPort.deleteReviewByReviewId(Mockito.anyLong())).thenReturn(Mono.empty());
        Mono<Void> voidMono = reviewService.deleteSingleReviewForSingleBook(5l);
        StepVerifier.create(voidMono)
                .verifyComplete();
    }


    private void compareTwoObjectRecursively(Object actual, Object expected) {
        Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expected);
    }

    private Flux<Review> buildMockFluxReview() {
        return Flux.fromIterable(
                List.of(buildMockReview(),buildMockReview(),buildMockReview())
        );
    }

    private Review buildMockReview() {
        return Review.builder()
                .reviewId(3l)
                .bookId(1l)
                .comment("nice")
                .build();
    }

    private Mono<SaveReviewCommand> buildMockMonoSaveReviewCommand() {
        return Mono.justOrEmpty(SaveReviewCommand.builder()
                .reviewId(3l)
                .bookId(1l)
                .comment("nice")
                .build());
    }

}