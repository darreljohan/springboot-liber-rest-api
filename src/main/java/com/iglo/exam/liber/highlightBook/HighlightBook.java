package com.iglo.exam.liber.highlightBook;
import com.iglo.exam.liber.book.Book;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "HighlightBooks")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class HighlightBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private Integer orderNumber;

    private LocalDateTime addedDate;

    @OneToOne(fetch =  FetchType.EAGER)
    @JoinColumn(name = "bookId", nullable = false)
    private Book book;




}
