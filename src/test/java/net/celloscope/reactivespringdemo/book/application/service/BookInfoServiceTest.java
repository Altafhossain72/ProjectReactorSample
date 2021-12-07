package net.celloscope.reactivespringdemo.book.application.service;

import net.celloscope.reactivespringdemo.book.application.port.out.BookInfoCRUDPort;
import net.celloscope.reactivespringdemo.book.domain.BookInfo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

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
                        bookInfo -> Assertions.assertThat(bookInfo)
                        .usingRecursiveComparison()
                        .ignoringCollectionOrder()
                        .isEqualTo(buildMockBookInfo())
                        ).verifyComplete();
    }

    private Mono<BookInfo> buildMockMonoBookInfo() {
      return   Mono.just(BookInfo.builder()
                .bookId(5l)
                .author("altaf")
                .title("my book")
                .ISBN("4734hdu").build());
    }
    private BookInfo buildMockBookInfo() {
        return   BookInfo.builder()
                .bookId(5l)
                .author("altaf")
                .title("my book")
                .ISBN("4734hdu").build();
    }

    @Test
    void loadAllBook() {
    }

    @Test
    void saveBookInfo() {
    }

    @Test
    void updateBookInfo() {
    }

    @Test
    void deleteBookInfo() {
    }
}