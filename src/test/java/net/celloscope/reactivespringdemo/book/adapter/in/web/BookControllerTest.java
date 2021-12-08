package net.celloscope.reactivespringdemo.book.adapter.in.web;

import net.celloscope.reactivespringdemo.book.adapter.out.persistence.entity.BookInfoDBEntity;
import net.celloscope.reactivespringdemo.book.adapter.out.persistence.repository.BookInfoRepository;
import net.celloscope.reactivespringdemo.book.application.port.in.BookInfoCRUDUseCase;
import net.celloscope.reactivespringdemo.book.application.port.in.LoadBookWithReviewsUseCase;
import net.celloscope.reactivespringdemo.book.application.port.in.ReviewCRUDUseCase;
import net.celloscope.reactivespringdemo.book.application.port.out.ReviewCRUDPort;
import net.celloscope.reactivespringdemo.book.application.service.BookInfoService;
import net.celloscope.reactivespringdemo.book.domain.BookInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.awt.print.Book;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@AutoConfigureWebTestClient
@SpringBootTest
class BookControllerTest {


    @MockBean
    BookInfoRepository bookInfoRepository;

    @Autowired
    WebTestClient webTestClient;

    @Test
    void getAllBook() {
        when(bookInfoRepository.findAll()).thenReturn(buildFluxOfBookInfoDBEntity());
        webTestClient.get().uri("/v1/all-book")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookInfo.class)
                .hasSize(2);

    }


    @Test
    void getBookById() {
        when(bookInfoRepository.findByBookId(Mockito.anyLong())).thenReturn(buildMonoOfBookInfoDBEntity());
        webTestClient.get().uri("/v1/book/2")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookInfo.class)
                .hasSize(1);
    }



    @Test
    void createBook() {
    }

    @Test
    void updateBook() {
    }

    @Test
    void deleteBook() {
    }

    @Test
    void getBookWithReviews() {
    }

    @Test
    void getAllBookWithReviews() {
    }


    private Flux<BookInfoDBEntity> buildFluxOfBookInfoDBEntity() {
        return Flux.
                fromIterable(
                        List.of(buildBookInfoDBEntity(), buildBookInfoDBEntity())
                );
    }

    private BookInfoDBEntity buildBookInfoDBEntity() {
        return BookInfoDBEntity.builder()
                .bookId(9l)
                .author("altaf")
                .ISBN("47dh")
                .title("the History")
                .build();
    }

    private Mono<BookInfoDBEntity> buildMonoOfBookInfoDBEntity() {
        return Mono.just(BookInfoDBEntity.builder()
                .bookId(9l)
                .author("altaf")
                .ISBN("47dh")
                .title("the History")
                .build());
    }
}