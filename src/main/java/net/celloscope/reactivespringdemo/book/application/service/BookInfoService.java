package net.celloscope.reactivespringdemo.book.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.celloscope.reactivespringdemo.book.application.port.in.BookInfoCRUDUseCase;
import net.celloscope.reactivespringdemo.book.application.port.in.SaveBookInfoCommand;
import net.celloscope.reactivespringdemo.book.application.port.out.BookInfoCRUDPort;
import net.celloscope.reactivespringdemo.book.domain.BookInfo;
import net.celloscope.reactivespringdemo.common.ExceptionHandlerUtil;
import net.celloscope.reactivespringdemo.common.Messages;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookInfoService implements BookInfoCRUDUseCase {

    private final BookInfoCRUDPort bookInfoCRUDPort;

    @Override
    public Mono<BookInfo> loadBookInfoById(Long bookId) {
        return bookInfoCRUDPort.loadBookInfoByBookId(bookId)
                .onErrorMap(throwable -> {
                    log.error("Exception Occurred while loadBookInfoByBookId :" + throwable);
                    return new ExceptionHandlerUtil(HttpStatus.INTERNAL_SERVER_ERROR, Messages.INTERNAL_SERVER_ERROR);
                }).log();
    }

    @Override
    public Flux<BookInfo> loadAllBook() {
        return bookInfoCRUDPort.loadAllBookInfo()
                .onErrorMap(throwable -> {
                    log.error("Exception Occurred while loadBookInfoByBookId :" + throwable);
                    return new ExceptionHandlerUtil(HttpStatus.INTERNAL_SERVER_ERROR, Messages.INTERNAL_SERVER_ERROR);
                }).log();
    }

    @Override
    public Mono<BookInfo> saveBookInfo(Mono<SaveBookInfoCommand> command) {
        return bookInfoCRUDPort.saveBookInfo(command)
                .onErrorMap(throwable -> {
                    log.error("Exception Occurred while saveBookInfo :" + throwable.getLocalizedMessage());
                    return new ExceptionHandlerUtil(HttpStatus.INTERNAL_SERVER_ERROR, Messages.INTERNAL_SERVER_ERROR);
                }).log();
    }

    @Override
    public Mono<BookInfo> updateBookInfo(Mono<SaveBookInfoCommand> command, Long bookId) {
        return bookInfoCRUDPort.updateBookInfo(command,bookId)
                .onErrorMap(throwable -> {
                    log.error("Exception Occurred while updateBookInfo :" + throwable);
                    return new ExceptionHandlerUtil(HttpStatus.INTERNAL_SERVER_ERROR, Messages.INTERNAL_SERVER_ERROR);
                }).log();
    }

    @Override
    public Mono<Void> deleteBookInfo(Long bookId) {
        return bookInfoCRUDPort.deleteBookInfoByBookId(bookId)
                .onErrorMap(throwable -> {
                    log.error("Exception Occurred while deleteBookInfoByBookId :" + throwable);
                    return new ExceptionHandlerUtil(HttpStatus.INTERNAL_SERVER_ERROR, Messages.INTERNAL_SERVER_ERROR);
                }).log();
    }
}
