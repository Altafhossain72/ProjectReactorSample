package net.celloscope.reactivespringdemo.book.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import net.celloscope.reactivespringdemo.book.adapter.out.persistence.entity.BookInfoDBEntity;
import net.celloscope.reactivespringdemo.book.adapter.out.persistence.repository.BookInfoRepository;
import net.celloscope.reactivespringdemo.book.application.port.in.SaveBookInfoCommand;
import net.celloscope.reactivespringdemo.book.application.port.out.BookInfoCRUDPort;
import net.celloscope.reactivespringdemo.book.domain.BookInfo;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class BookInfoPersistenceAdapter implements BookInfoCRUDPort {

    private final BookInfoRepository bookInfoRepository;

    ModelMapper modelMapper = new ModelMapper();


    @Override
    public Mono<BookInfo> loadBookInfoByBookId(Long bookId) {
      return bookInfoRepository.findByBookId(bookId)
               .map(bookInfoDBEntity -> modelMapper.map(bookInfoDBEntity,BookInfo.class));
    }

    @Override
    public Flux<BookInfo> loadAllBookInfo() {
       return bookInfoRepository.findAll()
               .map(bookInfoDBEntity -> modelMapper.map(bookInfoDBEntity,BookInfo.class));
    }



    @Override
    public Mono<BookInfo> saveBookInfo(Mono<SaveBookInfoCommand> command) {
      return command
                .map(d ->modelMapper.map(d, BookInfoDBEntity.class))
                .flatMap(bookInfoRepository::save)
                .map(bookInfoDBEntity -> modelMapper.map(bookInfoDBEntity,BookInfo.class));
    }

    @Override
    public Mono<BookInfo> updateBookInfo(Mono<SaveBookInfoCommand> command, Long bookId) {
      return bookInfoRepository.findByBookId(bookId)
               .flatMap(bookInfoDBEntity -> command.map(d ->modelMapper.map(d, BookInfoDBEntity.class)))
               .doOnNext(bookInfoDBEntity -> bookInfoDBEntity.setBookId(bookId))
               .flatMap(bookInfoRepository::save)
               .map(bookInfoDBEntity -> modelMapper.map(bookInfoDBEntity,BookInfo.class));
    }

    @Override
    public Mono<Void> deleteBookInfoByBookId(Long bookId) {
        return bookInfoRepository.deleteById(bookId);
    }
}
