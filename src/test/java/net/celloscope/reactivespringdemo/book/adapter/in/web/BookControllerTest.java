package net.celloscope.reactivespringdemo.book.adapter.in.web;

import net.celloscope.reactivespringdemo.book.adapter.out.persistence.entity.BookInfoDBEntity;
import net.celloscope.reactivespringdemo.book.adapter.out.persistence.entity.ReviewDBEntity;
import net.celloscope.reactivespringdemo.book.adapter.out.persistence.repository.BookInfoRepository;
import net.celloscope.reactivespringdemo.book.adapter.out.persistence.repository.ReviewRepository;
import net.celloscope.reactivespringdemo.book.application.port.in.SaveBookInfoCommand;
import net.celloscope.reactivespringdemo.book.application.port.in.SaveReviewCommand;
import net.celloscope.reactivespringdemo.book.domain.Book;
import net.celloscope.reactivespringdemo.book.domain.BookInfo;
import net.celloscope.reactivespringdemo.book.domain.Review;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.when;

@AutoConfigureWebTestClient
@SpringBootTest
class BookControllerTest {


    @MockBean
    BookInfoRepository bookInfoRepository;

    @MockBean
    ReviewRepository reviewRepository;

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
        webTestClient.get().uri("/v1/book/{bookId}",2l)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookInfo.class)
                .hasSize(1);
    }



    @Test
    void createBook() {
        when(bookInfoRepository.save(Mockito.any())).thenReturn(buildMonoOfBookInfoDBEntity());
        webTestClient.post().uri("/v1/create-book")
                .contentType(MediaType.APPLICATION_JSON)
                .body(buildMonoOfSaveBookInfoCommand(),SaveBookInfoCommand.class)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookInfo.class)
                .hasSize(1);

    }

    @Test
    void updateBook() {
        when(bookInfoRepository.findByBookId(Mockito.anyLong())).thenReturn(buildMonoOfBookInfoDBEntity());
        when(bookInfoRepository.save(Mockito.any())).thenReturn(buildMonoOfBookInfoDBEntity());
        webTestClient.put().uri("/v1/update-book/{bookId}",5l)
                .contentType(MediaType.APPLICATION_JSON)
                .body(buildMonoOfSaveBookInfoCommand(),SaveBookInfoCommand.class)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookInfo.class)
                .hasSize(1);
    }

    @Test
    void deleteBook() {
        when(bookInfoRepository.deleteById(Mockito.anyLong())).thenReturn(Mono.empty());
        webTestClient.delete().uri("/v1/delete-book/{bookId}",5l)
                .exchange()
                .expectStatus().isOk()
                ;
    }

    @Test
    void getBookWithReviews() {
        when(bookInfoRepository.findByBookId(Mockito.anyLong())).thenReturn(buildMonoOfBookInfoDBEntity());
        when(reviewRepository.findByBookId(Mockito.anyLong())).thenReturn(buildFluxOfReviewDBEntity());
        webTestClient.get().uri("/v1/book-with-review/{bookId}",2l)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Book.class)
                .hasSize(1);
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
        return Mono.just(buildBookInfoDBEntity());
    }

    private Mono<SaveBookInfoCommand> buildMonoOfSaveBookInfoCommand() {
        return Mono.justOrEmpty(SaveBookInfoCommand.builder()
                .author("altaf")
                .isbn("47dh")
                .title("the History")
                .build());
    }

    private Flux<ReviewDBEntity> buildFluxOfReviewDBEntity() {
        return Flux.fromIterable(List.of(buildReviewDbEntity(),buildReviewDbEntity()));
    }

    private ReviewDBEntity buildReviewDbEntity() {
        return ReviewDBEntity.builder()
                .bookId(5l)
                .reviewId(5l)
                .comment("altaf")
                .build();
    }
}