package net.celloscope.reactivespringdemo.book.application.port.in;

import net.celloscope.reactivespringdemo.book.domain.BookInfo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BookInfoCRUDUseCase {
   Mono<BookInfo> loadBookInfoById(Long bookId);
   Flux<BookInfo> loadAllBook();
   Mono<BookInfo> saveBookInfo(Mono<SaveBookInfoCommand> command);
   Mono<BookInfo> updateBookInfo(Mono<SaveBookInfoCommand> command, Long bookId);
   Mono<Void> deleteBookInfo(Long bookId);
}
