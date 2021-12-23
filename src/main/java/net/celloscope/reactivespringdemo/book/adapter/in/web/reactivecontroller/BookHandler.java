package net.celloscope.reactivespringdemo.book.adapter.in.web.reactivecontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.celloscope.reactivespringdemo.book.application.port.in.BookInfoCRUDUseCase;
import net.celloscope.reactivespringdemo.book.application.port.in.LoadBookWithReviewsUseCase;
import net.celloscope.reactivespringdemo.book.application.port.in.SaveBookInfoCommand;
import net.celloscope.reactivespringdemo.book.domain.Book;
import net.celloscope.reactivespringdemo.book.domain.BookInfo;
import net.celloscope.reactivespringdemo.common.ExceptionHandlerUtil;
import net.celloscope.reactivespringdemo.common.Messages;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.Duration;

@RequiredArgsConstructor
@Component
@Slf4j
public class BookHandler {

    private final BookInfoCRUDUseCase bookInfoCRUDUseCase;
    private final LoadBookWithReviewsUseCase loadBookWithReviewsUseCase;

    public Mono<ServerResponse> getAllBook() {
        return defaultReadResponse(
                bookInfoCRUDUseCase.loadAllBook()
                .delayElements(Duration.ofSeconds(2))
                .log()
                .switchIfEmpty(Mono.error(new ExceptionHandlerUtil(HttpStatus.NOT_FOUND, Messages.NOT_FOUND)))
                .onErrorMap(
                        throwable -> {
                            log.error("Exception Occurred while loadAllBook :" + throwable.getLocalizedMessage());
                            return new ResponseStatusException(HttpStatus.NOT_FOUND, throwable.getMessage(), throwable.getCause());
                        }
                ));
    }

    private static Mono<ServerResponse> defaultReadResponse(Publisher<BookInfo> books) {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(books, BookInfo.class);
    }


    public Mono<ServerResponse> getBookById(@PathVariable("bookId") @NotBlank(message = "bookId  can not be null") @NotEmpty(message = "bookId  can not be empty") Long bookId) {
        return defaultReadResponse(bookInfoCRUDUseCase.loadBookInfoById(bookId)
                .switchIfEmpty(Mono.error(new ExceptionHandlerUtil(HttpStatus.NOT_FOUND, Messages.NOT_FOUND)))
                .onErrorMap(
                        throwable -> {
                            log.error("Exception Occurred while loadBookInfoByBookId :" + throwable.getLocalizedMessage());
                            return new ResponseStatusException(HttpStatus.NOT_FOUND, throwable.getMessage(), throwable.getCause());
                        }
                ));
    }

    public Mono<ResponseEntity<BookInfo>> createBook(@RequestBody Mono<SaveBookInfoCommand> command) {
        return bookInfoCRUDUseCase.saveBookInfo(command)
                .map(ResponseEntity::ok)
                .onErrorMap(
                        throwable -> {
                            log.error("Exception Occurred while saveBookInfo :" + throwable.getLocalizedMessage());
                            return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, throwable.getMessage(), throwable.getCause());
                        }
                );
    }

    public Mono<ResponseEntity<BookInfo>> updateBook(@PathVariable("bookId") @NotBlank(message = "bookId  can not be null") @NotEmpty(message = "bookId  can not be empty") Long bookId,
                                                     @RequestBody Mono<SaveBookInfoCommand> command) {
        return this.bookInfoCRUDUseCase.updateBookInfo(command, bookId)
                .map(ResponseEntity::ok)
                .onErrorMap(
                        throwable -> {
                            log.error("Exception Occurred while updateBookInfo :" + throwable.getLocalizedMessage());
                            return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, throwable.getMessage(), throwable.getCause());
                        }
                );
    }

    public Mono<ResponseEntity<Void>> deleteBook(@PathVariable("bookId") @NotBlank(message = "bookId  can not be null") @NotEmpty(message = "bookId  can not be empty") Long bookId) {
        return this.bookInfoCRUDUseCase.deleteBookInfo(bookId)
                .map(ResponseEntity::ok)
                .onErrorMap(
                        throwable -> {
                            log.error("Exception Occurred while deleteBookInfo :" + throwable.getLocalizedMessage());
                            return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, throwable.getMessage(), throwable.getCause());
                        }
                );
    }



    public Mono<ResponseEntity<Book>> getBookWithReviews(@PathVariable("bookId") @NotBlank(message = "bookId  can not be null") @NotEmpty(message = "bookId  can not be empty") Long bookId) {
        return this.loadBookWithReviewsUseCase.loadBookWithReviewsByBookId(bookId)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new ExceptionHandlerUtil(HttpStatus.NOT_FOUND, Messages.NOT_FOUND)))
                .onErrorMap(
                        throwable -> {
                            log.error("Exception Occurred while loadBookWithReviewsByBookId :" + throwable.getLocalizedMessage());
                            return new ResponseStatusException(HttpStatus.NOT_FOUND, throwable.getMessage(), throwable.getCause());
                        }
                );
    }

    public Flux<Book> getAllBookWithReviews() {
        return this.loadBookWithReviewsUseCase.loadAllBooksWithAllReviews()
                .switchIfEmpty(Mono.error(new ExceptionHandlerUtil(HttpStatus.NOT_FOUND, Messages.NOT_FOUND)))
                .onErrorMap(
                        throwable -> {
                            log.error("Exception Occurred while loadAllBookWithAllReviews :" + throwable.getLocalizedMessage());
                            return new ResponseStatusException(HttpStatus.NOT_FOUND, throwable.getMessage(), throwable.getCause());
                        }
                );
    }

}
