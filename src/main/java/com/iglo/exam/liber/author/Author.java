package com.iglo.exam.liber.author;

import com.iglo.exam.liber.book.Book;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Authors")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String description;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    Set<Book> books = new HashSet<>();
}
