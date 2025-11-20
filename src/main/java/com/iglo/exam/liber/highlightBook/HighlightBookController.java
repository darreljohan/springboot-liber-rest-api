package com.iglo.exam.liber.highlightBook;

import com.iglo.exam.liber.highlightBook.dto.HighlightBookInsertRequest;
import com.iglo.exam.liber.highlightBook.dto.HighlightBookResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books/highlighted")
public class HighlightBookController {

    public final HighlightBookService highlightBookService;

    public HighlightBookController(HighlightBookService highlightBookService) {
        this.highlightBookService = highlightBookService;
    }

    @GetMapping()
    public ResponseEntity<Page<HighlightBookResponse>>  findAllHighlightBooks(@PageableDefault(sort = "id", size = 10, page=0) Pageable pageable) {
        return ResponseEntity.ok(highlightBookService.findAllHighlightBooks(pageable));
    }

    @GetMapping("{orderNumber}")
    public ResponseEntity<HighlightBookResponse> findHighlightBookByOrderNumber(@PathVariable Integer orderNumber) {
        return ResponseEntity.ok(highlightBookService.findHighlightBookByOrderNumber(orderNumber));
    }

    @PostMapping()
    public ResponseEntity<HighlightBookResponse> addHighlightBook(@RequestBody @Valid HighlightBookInsertRequest highlightBookInsertRequest) {
        return ResponseEntity.ok(highlightBookService.addHighlightBook(highlightBookInsertRequest));
    }

    @DeleteMapping("{orderNumber}")
    public ResponseEntity<Void> deleteHighlightBook(@PathVariable Integer orderNumber) {
        highlightBookService.deleteHighlightBookByBookOrderNumber(orderNumber);
        return ResponseEntity.noContent().build();
    }
}
