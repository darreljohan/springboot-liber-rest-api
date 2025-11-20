package com.iglo.exam.liber.book;

import com.iglo.exam.liber.book.dto.BookResponse;
import com.iglo.exam.liber.book.dto.BookUpsertRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("books")
public class BookController {

    public final BookService bookService;
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping()
    public ResponseEntity<Page<BookResponse>> findAllBooks(@PageableDefault(sort = "name", size = 10, page=0) Pageable pageable)  {
        return ResponseEntity.ok(bookService.findAllBooks(pageable));
    }

    @GetMapping("{id}")
    public ResponseEntity<BookResponse> findBookById(@PathVariable Integer id)  {
        System.out.println("enter findBookById");
        return ResponseEntity.ok(bookService.findBookById(id));
    }

    @PostMapping()
    public ResponseEntity<BookResponse> addNewBook(@RequestBody @Valid BookUpsertRequest bookUpsertRequest)  {
        return ResponseEntity.ok(bookService.addNewBook(bookUpsertRequest));
    }

    @PutMapping("{id}")
    public ResponseEntity<BookResponse> updateBookById(@PathVariable Integer id, @RequestBody @Valid BookUpsertRequest bookUpsertRequest)  {
        return ResponseEntity.ok(bookService.updateBookById(id, bookUpsertRequest));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteBookById(@PathVariable Integer id) {
        bookService.deleteBookById(id);
        return ResponseEntity.noContent().build();
    }
}
