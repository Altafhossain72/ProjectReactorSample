package net.celloscope.reactivespringdemo.book.adapter.in.web.reactiveController;

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
import org.springframework.web.reactive.function.server.ServerRequest;
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

    public Mono<ServerResponse> getAllBook(ServerRequest serverRequest) {
        return bookInfoResponse(
                bookInfoCRUDUseCase.loadAllBook()
                        .delayElements(Duration.ofSeconds(2))
                        .log()
                        .switchIfEmpty(Mono.error(new ExceptionHandlerUtil(HttpStatus.NOT_FOUND, Messages.NOT_FOUND)))
                        .onErrorMap(
                                throwable -> {
                                    log.error("Exception Occurred while loadAllBook :" + throwable.getLocalizedMessage());
                                    return new ResponseStatusException(HttpStatus.NOT_FOUND, throwable.getMessage(), throwable.getCause());
                                }
                        )
        );
    }


    public Mono<ServerResponse> getBookById(ServerRequest serverRequest) {
        return bookInfoResponse(
                bookInfoCRUDUseCase.loadBookInfoById(
                        Long.valueOf(serverRequest.pathVariable("bookId")))
        );
    }


//    onErrorResume(
//            throwable -> {
//        log.error("Exception Occurred while loadBookInfoByBookId :" + throwable.getLocalizedMessage());
//        return errorResponse(throwable.getMessage(),HttpStatus.NOT_FOUND) ;
//    }

    public Mono<ServerResponse> createBook(ServerRequest serverRequest) {
        return bookInfoResponse(bookInfoCRUDUseCase.saveBookInfo(serverRequest.bodyToMono(SaveBookInfoCommand.class)))
                .onErrorMap(
                        throwable -> {
                            log.error("Exception Occurred while saveBookInfo :" + throwable.getLocalizedMessage());
                            return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, throwable.getMessage(), throwable.getCause());
                        }
                );
    }


    public Mono<ServerResponse> updateBook(ServerRequest serverRequest) {
        return bookInfoResponse(bookInfoCRUDUseCase.updateBookInfo(serverRequest.bodyToMono(SaveBookInfoCommand.class),
                Long.valueOf(serverRequest.pathVariable("bookId"))))
                .onErrorMap(
                        throwable -> {
                            log.error("Exception Occurred while updateBookInfo :" + throwable.getLocalizedMessage());
                            return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, throwable.getMessage(), throwable.getCause());
                        }
                );
    }

    public Mono<ServerResponse> deleteBook(ServerRequest serverRequest) {
        return defaultVoidResponse(bookInfoCRUDUseCase.deleteBookInfo(
                Long.valueOf(serverRequest.pathVariable("bookId"))))
                .onErrorMap(
                        throwable -> {
                            log.error("Exception Occurred while deleteBookInfo :" + throwable.getLocalizedMessage());
                            return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, throwable.getMessage(), throwable.getCause());
                        }
                );
    }


    public Mono<ServerResponse> getBookWithReviews(ServerRequest serverRequest) {
        return bookResponse(loadBookWithReviewsUseCase.loadBookWithReviewsByBookId(Long.valueOf(serverRequest.pathVariable("bookId"))))
                .switchIfEmpty(Mono.error(new ExceptionHandlerUtil(HttpStatus.NOT_FOUND, Messages.NOT_FOUND)))
                .onErrorMap(
                        throwable -> {
                            log.error("Exception Occurred while loadBookWithReviewsByBookId :" + throwable.getLocalizedMessage());
                            return new ResponseStatusException(HttpStatus.NOT_FOUND, throwable.getMessage(), throwable.getCause());
                        }
                );
    }

    public Mono<ServerResponse> getAllBookWithReviews(ServerRequest serverRequest) {
        return bookResponse(loadBookWithReviewsUseCase.loadAllBooksWithAllReviews())
                .switchIfEmpty(Mono.error(new ExceptionHandlerUtil(HttpStatus.NOT_FOUND, Messages.NOT_FOUND)))
                .onErrorMap(
                        throwable -> {
                            log.error("Exception Occurred while loadAllBookWithAllReviews :" + throwable.getLocalizedMessage());
                            return new ResponseStatusException(HttpStatus.NOT_FOUND, throwable.getMessage(), throwable.getCause());
                        }
                );
    }


    private static Mono<ServerResponse> bookInfoResponse(Publisher<BookInfo> books) {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(books, BookInfo.class)
                .switchIfEmpty(
                        ServerResponse.notFound()
                                .build()
                );
    }

    private static Mono<ServerResponse> errorResponse(String msg, HttpStatus httpStatus) {
        return ServerResponse
                .status(httpStatus)
                .body(new ExceptionHandlerUtil(httpStatus,
                        msg),ExceptionHandlerUtil.class);
    }

    private static Mono<ServerResponse> bookResponse(Publisher<Book> book) {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(book, Book.class);
    }

    private static Mono<ServerResponse> defaultVoidResponse(Mono<Void> id) {
        return ServerResponse.ok().build();
    }
}
