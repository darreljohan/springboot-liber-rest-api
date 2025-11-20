package com.iglo.exam.liber.review;

import com.iglo.exam.liber.review.dto.AddRatingRequest;
import com.iglo.exam.liber.review.dto.AddReviewRequest;
import com.iglo.exam.liber.review.dto.ReviewResponse;
import com.iglo.exam.liber.review.dto.UpdateReviewRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users/{username}/reviews")
public class ReviewController {
    public final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("{bookId}")
    public ResponseEntity<ReviewResponse> findReviewByUsernameAndBookId(@PathVariable String username,
                                                                        @PathVariable Integer bookId) {
        return ResponseEntity.ok(reviewService.findReviewByUsernameAndBookId(username, bookId));
    }

    @PostMapping("{bookId}/track")
    public ResponseEntity<ReviewResponse> AddBookReview(@PathVariable String username,
                                                    @PathVariable Integer bookId,
                                                    @RequestBody @Valid AddReviewRequest addReviewRequest) {
        return ResponseEntity.ok(reviewService.addReviewByUsernameAndBookId(addReviewRequest, username, bookId));
    }

    @PostMapping("{bookId}/rate")
    public ResponseEntity<ReviewResponse> addBookRating(@PathVariable String username,
                                                        @PathVariable Integer bookId,
                                                        @RequestBody @Valid AddRatingRequest addRatingRequest) {
        return ResponseEntity.ok(reviewService.addRatingByUsernameAndBookId(addRatingRequest, username, bookId));
    }

    @PutMapping("{bookId}")
   public ResponseEntity<ReviewResponse> updateReviewAndRating(@PathVariable String username,
                                                                @PathVariable Integer bookId,
                                                                @RequestBody @Valid UpdateReviewRequest updateReviewRequest) {
        return ResponseEntity.ok(reviewService.updateReviewByUsernameAndBookId(updateReviewRequest, username, bookId));
    }

    @DeleteMapping("{bookId}")
    public ResponseEntity<ReviewResponse> deleteReview(@PathVariable String username,
                                                       @PathVariable Integer bookId) {
        reviewService.deleteReviewByUsernameAndBookId(username, bookId);
        return ResponseEntity.noContent().build();
    }
}
