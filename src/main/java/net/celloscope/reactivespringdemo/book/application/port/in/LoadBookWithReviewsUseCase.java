package net.celloscope.reactivespringdemo.book.application.port.in;

import net.celloscope.reactivespringdemo.book.domain.Book;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LoadBookWithReviewsUseCase {
    Mono<Book> loadBookWithReviewsByBookId(Long bookId);
    Flux<Book> loadAllBooksWithAllReviews();
}
