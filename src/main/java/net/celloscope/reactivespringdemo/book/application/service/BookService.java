package net.celloscope.reactivespringdemo.book.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.celloscope.reactivespringdemo.book.application.port.in.LoadBookWithReviewsUseCase;
import net.celloscope.reactivespringdemo.book.application.port.out.BookInfoCRUDPort;
import net.celloscope.reactivespringdemo.book.application.port.out.ReviewCRUDPort;
import net.celloscope.reactivespringdemo.book.domain.Book;
import net.celloscope.reactivespringdemo.book.domain.BookInfo;
import net.celloscope.reactivespringdemo.book.domain.Review;
import net.celloscope.reactivespringdemo.common.ExceptionHandlerUtil;
import net.celloscope.reactivespringdemo.common.Messages;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService implements LoadBookWithReviewsUseCase {

    private final BookInfoCRUDPort bookInfoCRUDPort;
    private final ReviewCRUDPort reviewCRUDPort;

    @Override
    public Mono<Book> loadBookWithReviewsByBookId(Long bookId) {
        var bookInfoMono = bookInfoCRUDPort.loadBookInfoByBookId(bookId);
        var reviewFlux = reviewCRUDPort.loadAllReviewByBookId(bookId).collectList();
        return bookInfoMono.zipWith(reviewFlux, (b, r) -> new Book(b, r))
                .onErrorMap(throwable -> {
                    log.error("Exception Occurred while loadBookWithReviewsByBookId :" + throwable);
                    return new ExceptionHandlerUtil(HttpStatus.NOT_FOUND, Messages.NOT_FOUND);
                }).log();
    }

    @Override
    public Flux<Book> loadAllBooksWithAllReviews() {
        Flux<BookInfo> bookInfoFlux = bookInfoCRUDPort.loadAllBookInfo();
        return bookInfoFlux
                .flatMap(bookInfo -> {
                    Mono<List<Review>> reviews = reviewCRUDPort.loadAllReviewByBookId(bookInfo.getBookId()).collectList();
                    return reviews.map(review -> new Book(bookInfo, review));
                })
                .onErrorMap(throwable -> {
                    log.error("Exception Occurred while loadAllBookWithAllReviews :" + throwable);
                    return new ExceptionHandlerUtil(HttpStatus.NOT_FOUND, Messages.NOT_FOUND);
                }).log();
    }
}
