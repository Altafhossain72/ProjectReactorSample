package net.celloscope.reactivespringdemo.book.adapter.in.web.reactivecontroller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class BookEndPointConfiguration {

    @Bean
    RouterFunction<ServerResponse> routes(BookHandler handler){
        return route(GET("/v1/all-book"),handler::getAllBook)
                .andRoute(GET("/v1/book/{bookId}"), handler::getBookById);
    }
}
