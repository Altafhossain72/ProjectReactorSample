package net.celloscope.reactivespringdemo.book.adapter.in.web.reactiveController;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class BookEndPointConfiguration {

    @Bean
    RouterFunction<ServerResponse> routes(BookHandler handler){
        String path = "/v1/reactive/";
        return route(GET(path+"all-book"),handler::getAllBook)
                .andRoute(GET(path+"book/{bookId}"), handler::getBookById)
                .andRoute(POST(path+"create-book"),handler::createBook)
                .andRoute(PUT(path+"update-book/{bookId}"),handler::updateBook)
                .andRoute(DELETE(path+"delete-book/{bookId}"), handler::deleteBook)
                .andRoute(GET(path+"book-with-review/{bookId}"), handler::getBookWithReviews)
                .andRoute(GET(path+"all-book-with-reviews"), handler::getAllBookWithReviews);
    }
}
