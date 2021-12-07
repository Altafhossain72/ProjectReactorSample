create table books
(
    bookId bigserial not null ,
    title varchar(32) not null,
    author varchar(32),
    ISBN varchar(128),
    CONSTRAINT pk_books_bookId
        PRIMARY KEY(bookId)

);

create table reviews
(
    reviewId bigserial not null ,
    bookId bigint not null,
    comment varchar(32),

    CONSTRAINT pk_reviews_reviewId
        PRIMARY KEY(reviewId),

    CONSTRAINT fk_reviews_bookId
        FOREIGN KEY(bookId)
            REFERENCES books(bookId)

);

insert into books(title,author,isbn)
values('the history','altaf','IOjejeu'),
      ('Head First java','altaf','IOjejddeu'),
      ('The Complete Refference','altaf','IOjesssjeu'),
      (' Ulysses','ames Joyce','IOerrt3jejeu');

insert into reviews(bookId,comment)
values('1','Nice book'),
      ('1','awesome book'),
      ('2','best book'),
      ('2','best book'),
      ('3','nicest book'),
      ('3','nicest book'),
      ('4','nicest book'),
      ('4','latest book');