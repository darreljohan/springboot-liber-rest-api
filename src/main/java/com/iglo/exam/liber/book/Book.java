package com.iglo.exam.liber.book;


import com.iglo.exam.liber.author.Author;
import com.iglo.exam.liber.highlightBook.HighlightBook;
import com.iglo.exam.liber.review.Review;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "books")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String cover;
    private LocalDate releaseDate;
    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "authorId", nullable = false)
    private Author author;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Review> reviewSet = new HashSet<>();

    @OneToOne(mappedBy = "book",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true)
    private HighlightBook highlightBook;

}
