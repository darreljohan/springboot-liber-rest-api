package com.iglo.exam.liber.author;

import org.springframework.data.jpa.repository.JpaRepository;



public interface AuthorRepository extends JpaRepository<Author, Integer> {

        boolean existsByName(String name);

}
