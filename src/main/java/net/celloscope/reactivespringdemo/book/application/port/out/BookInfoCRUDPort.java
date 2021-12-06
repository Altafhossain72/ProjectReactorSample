package net.celloscope.reactivespringdemo.book.application.port.out;

import net.celloscope.reactivespringdemo.book.application.port.in.SaveBookInfoCommand;
import net.celloscope.reactivespringdemo.book.domain.BookInfo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BookInfoCRUDPort {
  Mono<BookInfo> loadBookInfoByBookId(Long bookId);
  Flux<BookInfo> loadAllBookInfo();
  Mono<BookInfo> saveBookInfo(Mono<SaveBookInfoCommand> command);
  Mono<BookInfo> updateBookInfo(Mono<SaveBookInfoCommand> command, Long bookId);
  Mono<Void> deleteBookInfoByBookId(Long bookId);
}
