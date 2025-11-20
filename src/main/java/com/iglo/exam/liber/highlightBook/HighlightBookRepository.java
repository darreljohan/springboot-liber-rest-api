package com.iglo.exam.liber.highlightBook;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface HighlightBookRepository extends JpaRepository<HighlightBook, Integer> {

    Optional<HighlightBook> findByOrderNumber(Integer orderNumber);

    Optional<HighlightBook> findByBookId(Integer bookId);

    void deleteByBookId(Integer bookId);

    @Modifying
    @Query("DELETE FROM HighlightBook h WHERE h.orderNumber = :orderNumber")
    void deleteByOrderNumber(Integer orderNumber);
}
