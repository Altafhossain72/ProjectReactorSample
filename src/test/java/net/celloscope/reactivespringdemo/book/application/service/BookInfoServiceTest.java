package net.celloscope.reactivespringdemo.book.application.service;

import net.celloscope.reactivespringdemo.book.application.port.in.SaveBookInfoCommand;
import net.celloscope.reactivespringdemo.book.application.port.out.BookInfoCRUDPort;
import net.celloscope.reactivespringdemo.book.domain.BookInfo;
import net.celloscope.reactivespringdemo.common.ExceptionHandlerUtil;
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

import static net.celloscope.reactivespringdemo.common.Messages.INTERNAL_SERVER_ERROR;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class BookInfoServiceTest {

    @Mock
    BookInfoCRUDPort bookInfoCRUDPort;

    @InjectMocks
    BookInfoService bookInfoService;

    @Test
    void loadBookInfoById() {
        when(bookInfoCRUDPort.loadBookInfoByBookId(Mockito.anyLong())).thenReturn(buildMockMonoBookInfo());
        Mono<BookInfo> bookInfoMono = bookInfoService.loadBookInfoById(5l);
        StepVerifier.create(bookInfoMono)
                .assertNext(
                       bookInfo -> compareTwoObjectRecursively(bookInfo,buildMockBookInfo2())
                        ).verifyComplete();
    }

    @Test
    void loadBookInfoByIdErrorMatch() {
        when(bookInfoCRUDPort.loadBookInfoByBookId(Mockito.anyLong())).thenReturn(
                Mono.error(new IllegalArgumentException("Some message")));
        Mono<BookInfo> bookInfoMono = bookInfoService.loadBookInfoById(5l);
        StepVerifier.create(bookInfoMono)
                .expectErrorMatches(throwable -> throwable instanceof ExceptionHandlerUtil &&
                        throwable.getMessage().equals(INTERNAL_SERVER_ERROR)).verify();
    }



    @Test
    void loadAllBook() {
        when(bookInfoCRUDPort.loadAllBookInfo()).thenReturn(buildMockFluxBookInfo());
        Flux<BookInfo> bookInfoFlux = bookInfoService.loadAllBook();
        StepVerifier.create(bookInfoFlux)
                .assertNext(
                        bookInfo -> compareTwoObjectRecursively(bookInfo,buildMockBookInfo())
                ).assertNext(
                        bookInfo -> compareTwoObjectRecursively(bookInfo,buildMockBookInfo2())
                ).assertNext(
                        bookInfo -> compareTwoObjectRecursively(bookInfo,buildMockBookInfo3())
                )
                .verifyComplete();
    }

    @Test
    void loadAllBookErrorMatch() {
        when(bookInfoCRUDPort.loadAllBookInfo()).thenReturn(
                Flux.error(new IllegalArgumentException("Some message")));
        Flux<BookInfo> bookInfoFlux = bookInfoService.loadAllBook();
        StepVerifier.create(bookInfoFlux)
                .expectErrorMatches(throwable -> throwable instanceof ExceptionHandlerUtil &&
                        throwable.getMessage().equals(INTERNAL_SERVER_ERROR)).verify();
    }



    @Test
    void saveBookInfo() {
        when(bookInfoCRUDPort.saveBookInfo(Mockito.any())).thenReturn(buildMockMonoBookInfo());
        Mono<BookInfo> bookInfoMono = bookInfoService.saveBookInfo(buildMockMonoSaveBookInfoCommand());
        StepVerifier.create(bookInfoMono)
                .assertNext(
                        bookInfo -> compareTwoObjectRecursively(bookInfo,buildMockBookInfo2())
                ).verifyComplete();
    }

    @Test
    void saveBookInfoErrorMatch() {
        when(bookInfoCRUDPort.saveBookInfo(Mockito.any())).thenReturn(
                Mono.error(new IllegalArgumentException("Some message")));
        Mono<BookInfo> bookInfoMono = bookInfoService.saveBookInfo(buildMockMonoSaveBookInfoCommand());
        StepVerifier.create(bookInfoMono)
                .expectErrorMatches(throwable -> throwable instanceof ExceptionHandlerUtil &&
                        throwable.getMessage().equals(INTERNAL_SERVER_ERROR)).verify();
    }

    @Test
    void updateBookInfo() {
        when(bookInfoCRUDPort.updateBookInfo(Mockito.any(),Mockito.anyLong())).thenReturn(buildMockMonoBookInfo());
        Mono<BookInfo> updatedBookInfo = bookInfoService.updateBookInfo(buildMockMonoSaveBookInfoCommand(), 5l);
        StepVerifier.create(updatedBookInfo)
                .assertNext(
                        bookInfo -> compareTwoObjectRecursively(bookInfo,buildMockBookInfo2())
                ).verifyComplete();
    }

    @Test
    void updateBookInfoErrorMatch() {
        when(bookInfoCRUDPort.updateBookInfo(Mockito.any(),Mockito.anyLong())).thenReturn(
                Mono.error(new IllegalArgumentException("Some message")));
        Mono<BookInfo> bookInfoMono = bookInfoService.updateBookInfo(buildMockMonoSaveBookInfoCommand(),5l);
        StepVerifier.create(bookInfoMono)
                .expectErrorMatches(throwable -> throwable instanceof ExceptionHandlerUtil &&
                        throwable.getMessage().equals(INTERNAL_SERVER_ERROR)).verify();
    }

    @Test
    void deleteBookInfo() {
        when(bookInfoCRUDPort.deleteBookInfoByBookId(Mockito.anyLong())).thenReturn(Mono.empty());
        Mono<Void> voidMono = bookInfoService.deleteBookInfo(5l);
        StepVerifier.create(voidMono)
                .verifyComplete();
    }

    private void compareTwoObjectRecursively(Object actual, Object expected) {
        Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expected);
    }

    private Flux<BookInfo> buildMockFluxBookInfo() {
        return Flux.fromIterable(
                List.of(buildMockBookInfo(),buildMockBookInfo2(),buildMockBookInfo3())
        );
    }

    private Mono<BookInfo> buildMockMonoBookInfo() {
        return   Mono.just(BookInfo.builder()
                .bookId(5l)
                .author("altaf")
                .title("my book")
                .ISBN("4734hdu").build());
    }
    private Mono<SaveBookInfoCommand> buildMockMonoSaveBookInfoCommand() {
        return   Mono.just(SaveBookInfoCommand.builder()
                .bookId(5l)
                .author("altaf")
                .title("my book")
                .isbn("4734hdu").build());
    }
    private BookInfo buildMockBookInfo() {
        return   BookInfo.builder()
                .bookId(4l)
                .author("altaf")
                .title("my book")
                .ISBN("4734hdu").build();
    }
    private BookInfo buildMockBookInfo2() {
        return   BookInfo.builder()
                .bookId(5l)
                .author("altaf")
                .title("my book")
                .ISBN("4734hdu").build();
    }
    private BookInfo buildMockBookInfo3() {
        return   BookInfo.builder()
                .bookId(6l)
                .author("altaf")
                .title("my book")
                .ISBN("4734hdu").build();
    }
}