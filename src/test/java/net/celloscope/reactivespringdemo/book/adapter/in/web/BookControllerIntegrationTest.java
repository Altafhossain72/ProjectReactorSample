package net.celloscope.reactivespringdemo.book.adapter.in.web;


import net.celloscope.reactivespringdemo.book.domain.BookInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.ExchangeFunctions;
import reactor.test.StepVerifier;

import java.net.URI;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerIntegrationTest {

    @LocalServerPort
    private Integer port;

    private String baseUrl = "http://localhost";

    private static ExchangeFunction exchange;

    @BeforeAll
    static void init() {
        exchange = ExchangeFunctions.create(new ReactorClientHttpConnector());
    }

    @BeforeEach
    void setUp() {
        baseUrl = baseUrl.concat(":").concat(port.toString()).concat("/v1");
    }

    @Test
    void shouldReturnAllBook(){
        URI uri = URI.create(baseUrl+"/all-book");
        ClientRequest request = ClientRequest.create(HttpMethod.GET, uri).build();
        exchange.exchange(request).flatMapMany
                (response -> response.bodyToFlux(BookInfo.class))
                .as(StepVerifier::create)
                .expectNextCount(6)
                .verifyComplete();
    }


    @Test
    void shouldReturnABook(){
        URI uri = URI.create(baseUrl+"/book/5");
        ClientRequest request = ClientRequest.create(HttpMethod.GET, uri).build();
        exchange.exchange(request).flatMapMany
                        (response -> response.bodyToMono(BookInfo.class))
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

}
