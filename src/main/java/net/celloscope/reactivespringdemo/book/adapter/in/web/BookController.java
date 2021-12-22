package net.celloscope.reactivespringdemo.book.adapter.in.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.celloscope.reactivespringdemo.book.application.port.in.BookInfoCRUDUseCase;
import net.celloscope.reactivespringdemo.book.application.port.in.LoadBookWithReviewsUseCase;
import net.celloscope.reactivespringdemo.book.application.port.in.SaveBookInfoCommand;
import net.celloscope.reactivespringdemo.book.domain.Book;
import net.celloscope.reactivespringdemo.book.domain.BookInfo;
import net.celloscope.reactivespringdemo.common.ExceptionHandlerUtil;
import net.celloscope.reactivespringdemo.common.Messages;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@RequiredArgsConstructor
@RestController
@Validated
@Slf4j
@RequestMapping("/v1")
public class BookController {

    private final BookInfoCRUDUseCase bookInfoCRUDUseCase;
    private final LoadBookWithReviewsUseCase loadBookWithReviewsUseCase;



    @GetMapping("/all-book")
    public Flux<BookInfo> getAllBook() {
        return bookInfoCRUDUseCase.loadAllBook()
                .log()
                .switchIfEmpty(Mono.error(new ExceptionHandlerUtil(HttpStatus.NOT_FOUND, Messages.NOT_FOUND)))
                .onErrorMap(
                        throwable -> {
                            log.error("Exception Occurred while loadAllBook :" + throwable.getLocalizedMessage());
                            return new ResponseStatusException(HttpStatus.NOT_FOUND, throwable.getMessage(), throwable.getCause());
                        }
                );
    }

    @GetMapping("/book/{bookId}")
    public Mono<ResponseEntity<BookInfo>> getBookById(@PathVariable("bookId") @NotBlank(message = "bookId  can not be null") @NotEmpty(message = "bookId  can not be empty") Long bookId) {
        return this.bookInfoCRUDUseCase.loadBookInfoById(bookId)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new ExceptionHandlerUtil(HttpStatus.NOT_FOUND, Messages.NOT_FOUND)))
                .onErrorMap(
                        throwable -> {
                            log.error("Exception Occurred while loadBookInfoByBookId :" + throwable.getLocalizedMessage());
                           return new ResponseStatusException(HttpStatus.NOT_FOUND, throwable.getMessage(), throwable.getCause());
                        }
                );
    }

    @PostMapping("/create-book")
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

    @PutMapping("/update-book/{bookId}")
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

    @DeleteMapping("/delete-book/{bookId}")
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

    @GetMapping("/book-with-review/{bookId}")
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


    @GetMapping("/all-book-with-reviews")
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
