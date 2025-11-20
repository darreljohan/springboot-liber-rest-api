package com.iglo.exam.liber.author;

import com.iglo.exam.liber.author.dto.AuthorResponse;
import com.iglo.exam.liber.author.dto.AuthorUpsertRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("authors")
public class AuthorController {
    private  final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping()
    public ResponseEntity<Page<AuthorResponse>> findAllAuthors(@PageableDefault(sort = "name", size = 10, page=0) Pageable pageable) {
        return ResponseEntity.ok(authorService.findALlAuthor(pageable));
    }

    @GetMapping("{id}")
    public ResponseEntity<AuthorResponse> findAuthorById(@PathVariable Integer id) {
        return ResponseEntity.ok(authorService.findAuthorById(id));
    }

    @PostMapping()
    public ResponseEntity<AuthorResponse> addAuthor(@RequestBody @Valid AuthorUpsertRequest authorRequest) {
        return ResponseEntity.status(201).body(authorService.addNewAuthor(authorRequest));
    }

    @PutMapping("{id}")
    public ResponseEntity<AuthorResponse> updateAuthorById(@PathVariable Integer id, @RequestBody @Valid AuthorUpsertRequest authorUpsertRequest) {
        return ResponseEntity.ok(authorService.updateAuthorById(id, authorUpsertRequest));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteAuthorById(@PathVariable Integer id) {
        authorService.deleteAuthorById(id);
        return ResponseEntity.noContent().build();
    }
}
