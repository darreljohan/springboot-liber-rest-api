package com.iglo.exam.liber.review;

import com.iglo.exam.liber.book.Book;
import com.iglo.exam.liber.book.BookRepository;
import com.iglo.exam.liber.error.exception.ResourceNotFound;
import com.iglo.exam.liber.review.dto.AddRatingRequest;
import com.iglo.exam.liber.review.dto.AddReviewRequest;
import com.iglo.exam.liber.review.dto.ReviewResponse;
import com.iglo.exam.liber.review.dto.UpdateReviewRequest;
import com.iglo.exam.liber.user.User;
import com.iglo.exam.liber.user.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReviewService {
    public final ReviewRepository reviewRepository;
    public final UserRepository userRepository;
    public final BookRepository bookRepository;
    public final ReviewDtoMapper reviewDtoMapper;

    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository, BookRepository bookRepository, ReviewDtoMapper reviewDtoMapper) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.reviewDtoMapper = reviewDtoMapper;
    }

    public ReviewResponse findReviewByUsernameAndBookId(String username, Integer bookId) {
        User user = userRepository.findByUsernameAndDeactivatedFalse(username)
                .orElseThrow(() -> new ResourceNotFound("User not found with username: " + username));
        Book book = bookRepository.findByIdAndIsDeletedFalse(bookId)
                .orElseThrow(() -> new ResourceNotFound("Book not found with id: " + bookId));
        Review review = reviewRepository.findByUser_UsernameAndBook_id(username, bookId)
                .orElseThrow(() -> new ResourceNotFound("Reviews not found for user: " + username + " and bookId: " + bookId));
        return reviewDtoMapper.toReviewResponse(review);
    }

    public ReviewResponse addReviewByUsernameAndBookId(AddReviewRequest addReviewRequest, String username, Integer bookId) {
        User user = userRepository.findByUsernameAndDeactivatedFalse(username)
                .orElseThrow(() -> new ResourceNotFound("User not found with username: " + username));
        Book book = bookRepository.findByIdAndIsDeletedFalse(bookId)
                .orElseThrow(() -> new ResourceNotFound("Book not found with id: " + bookId));

        LocalDateTime localDateTime = LocalDateTime.now();
        ReviewId reviewId = ReviewId.builder().userId(user.getId()).bookId(book.getId()).build();
        Review reviewTarget = reviewRepository.findByUser_UsernameAndBook_id(username, bookId)
                .orElseGet(() -> {
                    return Review.builder()
                            .id(reviewId)
                            .addedDate(localDateTime.toLocalDate())
                            .user(user)
                            .book(book)
                            .build();
                });

        reviewTarget.setReadStatus(addReviewRequest.getReadStatus());
        reviewTarget.setReviewDate(localDateTime);
        Review result = reviewRepository.save(reviewTarget);
        return reviewDtoMapper.toReviewResponse(result);
    }

    public ReviewResponse addRatingByUsernameAndBookId(AddRatingRequest addRatingRequest, String username, Integer bookId) {
        User user = userRepository.findByUsernameAndDeactivatedFalse(username)
                .orElseThrow(() -> new ResourceNotFound("User not found with username: " + username));
        Book book = bookRepository.findByIdAndIsDeletedFalse(bookId)
                .orElseThrow(() -> new ResourceNotFound("Book not found with id: " + bookId));

        LocalDateTime localDateTime = LocalDateTime.now();
        ReviewId reviewId = ReviewId.builder().userId(user.getId()).bookId(book.getId()).build();
        Review reviewTarget = reviewRepository.findByUser_UsernameAndBook_id(username, bookId)
                .orElseGet(() -> {
                    return Review.builder()
                            .id(reviewId)
                            .addedDate(localDateTime.toLocalDate())
                            .user(user)
                            .book(book)
                            .build();
                });

        reviewTarget.setRating(addRatingRequest.getRating());
        reviewTarget.setReadStatus(addRatingRequest.getReadStatus());
        reviewTarget.setReviewDate(localDateTime);;
        reviewTarget.setFinishedReadingDate( addRatingRequest.getFinishedReadingDate().toLocalDate());
        Review result = reviewRepository.save(reviewTarget);
        return reviewDtoMapper.toReviewResponse(result);
    }

    public ReviewResponse updateReviewByUsernameAndBookId(UpdateReviewRequest updateReviewRequest, String username, Integer bookId) {
        User user = userRepository.findByUsernameAndDeactivatedFalse(username)
                .orElseThrow(() -> new ResourceNotFound("User not found with username: " + username));
        Book book = bookRepository.findByIdAndIsDeletedFalse(bookId)
                .orElseThrow(() -> new ResourceNotFound("Book not found with id: " + bookId));
        Review review = reviewRepository.findByUser_UsernameAndBook_id(username, bookId)
                .orElseThrow(() -> new ResourceNotFound("Review not found for user: " + username + " and bookId: " + bookId));



        review.setReadStatus(updateReviewRequest.getReadStatus());
        review.setRating(updateReviewRequest.getRating());
        review.setTitle(updateReviewRequest.getTitle());
        review.setDescription(updateReviewRequest.getDescription());
        review.setFinishedReadingDate(updateReviewRequest.getFinishedReadingDate().toLocalDate());

        Review result = reviewRepository.save(review);
        return reviewDtoMapper.toReviewResponse(result);
    }

    public void deleteReviewByUsernameAndBookId(String username, Integer bookId) {
        Review review = reviewRepository.findByUser_UsernameAndBook_id(username, bookId)
                .orElseThrow(() -> new ResourceNotFound("Review not found for user: " + username + " and bookId: " + bookId));
        reviewRepository.delete(review);
    }
}
