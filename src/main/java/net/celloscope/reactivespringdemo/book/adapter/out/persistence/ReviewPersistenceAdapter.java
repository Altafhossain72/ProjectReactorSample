package net.celloscope.reactivespringdemo.book.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import net.celloscope.reactivespringdemo.book.adapter.out.persistence.entity.ReviewDBEntity;
import net.celloscope.reactivespringdemo.book.adapter.out.persistence.repository.ReviewRepository;
import net.celloscope.reactivespringdemo.book.application.port.in.SaveReviewCommand;
import net.celloscope.reactivespringdemo.book.application.port.out.ReviewCRUDPort;
import net.celloscope.reactivespringdemo.book.domain.Review;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Controller
@RequiredArgsConstructor
public class ReviewPersistenceAdapter implements ReviewCRUDPort {
    private final ReviewRepository reviewRepository;

    ModelMapper modelMapper = new ModelMapper();

    @Override
    public Flux<Review> loadAllReviewByBookId(Long bookId) {
        return reviewRepository.findByBookId(bookId)
                .map(reviewDBEntity -> modelMapper.map(reviewDBEntity, Review.class));
    }

    @Override
    public Mono<Review> saveReview(Mono<SaveReviewCommand> command) {
        return command
                .map(d -> modelMapper.map(d, ReviewDBEntity.class))
                .flatMap(reviewRepository::save)
                .map(reviewDBEntity -> modelMapper.map(reviewDBEntity, Review.class));
    }

    @Override
    public Mono<Review> updateReview(Mono<SaveReviewCommand> command, Long reviewId) {
        return reviewRepository.findByReviewId(reviewId)
                .flatMap(reviewDbEntity -> command.map(d -> modelMapper.map(d, ReviewDBEntity.class)))
                .doOnNext(reviewDBEntity -> reviewDBEntity.setReviewId(reviewId))
                .flatMap(reviewRepository::save)
                .map(reviewDBEntity -> modelMapper.map(reviewDBEntity, Review.class));
    }

    @Override
    public Mono<Void> deleteReviewByBookId(Long bookId) {
        return reviewRepository.deleteByBookId(bookId);
    }

    @Override
    public Mono<Void> deleteReviewByReviewId(Long reviewId) {
        return reviewRepository.deleteById(reviewId);
    }
}
