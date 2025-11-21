package com.iglo.exam.liber.book;

import com.iglo.exam.liber.book.dto.BookCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Integer> {
    Page<Book> findByIsDeletedFalse(Pageable pageable);

    Optional<Book> findByIdAndIsDeletedFalse(Integer id);

    List<Book> findByAuthor_IdAndIsDeletedFalse(Integer authorId);

 }
