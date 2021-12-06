create table books
(
    bookId bigint SERIAL not null ,
    title varchar(32) not null,
    author varchar(32) not null,
    ISBN varchar(128),
    CONSTRAINT pk_books_bookId
        PRIMARY KEY(bookId)

);

create table reviews
(
    reviewId bigint SERIAL not null ,
    bookId bigint not null,
    comment varchar(32) not null,

    CONSTRAINT pk_reviews_reviewId
        PRIMARY KEY(reviewId),

    CONSTRAINT fk_reviews_bookId
        FOREIGN KEY(bookId)
            REFERENCES books(bookId)

);

