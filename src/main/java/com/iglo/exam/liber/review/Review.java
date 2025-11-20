package com.iglo.exam.liber.review;
import com.iglo.exam.liber.book.Book;
import com.iglo.exam.liber.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "Reviews")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Review {
    @EmbeddedId
    private ReviewId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("bookId")
    @JoinColumn(name = "bookId")
    private Book book;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("userId")
    @JoinColumn(name = "userId")
    private User user;

    private String title;

    @Column(length = 8000, columnDefinition = "TEXT")
    private String description;
    private Integer rating;
    private LocalDate addedDate;

    @Enumerated(EnumType.STRING)
    private ReadStatus readStatus;

    private LocalDate finishedReadingDate;
    private LocalDateTime reviewDate;
}
