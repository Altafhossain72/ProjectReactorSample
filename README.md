# ProjectReactorSample

A small Spring WebFlux + R2DBC project (2022) that shows how I lay out a reactive service in hexagonal (ports and adapters) style. Domain: books and their reviews.

## Layout

```
book/
  domain/                 Book, BookInfo, Review            (plain objects, no framework)
  application/
    port/in/              use cases the web layer calls      (BookInfoCRUDUseCase, LoadBookWithReviewsUseCase, ReviewCRUDUseCase)
    port/out/             what the application needs         (BookInfoCRUDPort, ReviewCRUDPort)
    service/              use-case implementations
  adapter/
    in/web/restController/       annotated @RestController endpoints under /v1
    in/web/reactiveController/   the same endpoints as functional routes (RouterFunction + handler)
    out/persistence/             R2DBC repositories and entities behind the out-ports
```

The same use cases are exposed twice on purpose, once with annotated controllers and once with functional routing, to compare the two WebFlux styles. `all-book` streams as NDJSON.

## Endpoints

`GET /v1/all-book` · `GET /v1/book/{id}` · `POST /v1/create-book` · `PUT /v1/update-book/{id}` · `DELETE /v1/delete-book/{id}` · `GET /v1/book-with-review/{id}` · `GET /v1/all-book-with-reviews` · `GET /v1/review/{bookId}` · `POST /v1/create-review` · `PUT /v1/update-review/{id}` · `DELETE /v1/delete-all-review/{bookId}`

A Postman collection is in `src/main/resources/`.

## Run

Needs Java 11+ and a local PostgreSQL (`spring.r2dbc.url` in `application.properties`; `data.sql` seeds it on start).

```
./gradlew bootRun
./gradlew test        # WebTestClient integration tests
```
