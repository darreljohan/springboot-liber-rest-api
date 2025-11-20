package com.iglo.exam.liber.review;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    Optional<Review> findByUser_UsernameAndBook_id(String username, Integer bookId);

}
