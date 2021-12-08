package net.celloscope.reactivespringdemo.book.application.service;

import net.celloscope.reactivespringdemo.book.application.port.out.BookInfoCRUDPort;
import net.celloscope.reactivespringdemo.book.application.port.out.ReviewCRUDPort;
import net.celloscope.reactivespringdemo.book.domain.Book;
import net.celloscope.reactivespringdemo.book.domain.BookInfo;
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
class BookServiceTest {

    @Mock
    BookInfoCRUDPort bookInfoCRUDPort;

    @Mock
    ReviewCRUDPort reviewCRUDPort;

    @InjectMocks
    BookService bookService;

    @Test
    void loadBookWithReviewsByBookId() {
        when(bookInfoCRUDPort.loadBookInfoByBookId(Mockito.anyLong())).thenReturn(buildMockMonoBookInfo());
        when(reviewCRUDPort.loadAllReviewByBookId(Mockito.anyLong())).thenReturn(buildMockMonoReviewList());
        Mono<Book> bookMono = bookService.loadBookWithReviewsByBookId(5l);
        StepVerifier.create(bookMono)
                .assertNext(
                        book -> compareTwoObjectRecursively(book, buildMockBook())
                )
                .verifyComplete();
    }


    @Test
    void loadAllBookWithAllReviews() {
        when(bookInfoCRUDPort.loadAllBookInfo()).thenReturn(buildMockFluxBookInfo());
        when(reviewCRUDPort.loadAllReviewByBookId(Mockito.anyLong())).thenReturn(buildMockMonoReviewList());
        Flux<Book> bookFlux = bookService.loadAllBooksWithAllReviews();
        StepVerifier.create(bookFlux)
                .assertNext(
                        book -> compareTwoObjectRecursively(book, buildMockBook())
                )
                .assertNext(
                        book -> compareTwoObjectRecursively(book, buildMockBook())
                )
                .verifyComplete();
    }


        private Flux<BookInfo> buildMockFluxBookInfo() {
            return Flux.fromIterable(
                    List.of(buildMockBookInfo(),buildMockBookInfo())
            );

    }


    private void compareTwoObjectRecursively(Object actual, Object expected) {
        Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expected);
    }

    private Mono<BookInfo> buildMockMonoBookInfo() {
        return Mono.just(BookInfo.builder()
                .bookId(5l)
                .author("altaf")
                .title("my book")
                .ISBN("4734hdu").build());
    }

    private BookInfo buildMockBookInfo() {
        return BookInfo.builder()
                .bookId(5l)
                .author("altaf")
                .title("my book")
                .ISBN("4734hdu").build();
    }

    private Review buildMockReview() {
        return Review.builder()
                .reviewId(3l)
                .bookId(1l)
                .comment("nice")
                .build();
    }

    private Flux<Review> buildMockMonoReviewList() {
        return Flux.fromIterable(buildReviewList());
    }

    private List<Review> buildReviewList() {
        return List.of(buildMockReview(), buildMockReview());
    }

    private Book buildMockBook() {
        return Book.builder()
                .bookInfo(buildMockBookInfo())
                .review(buildReviewList())
                .build();
    }

}