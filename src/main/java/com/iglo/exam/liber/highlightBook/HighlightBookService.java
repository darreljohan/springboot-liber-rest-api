package com.iglo.exam.liber.highlightBook;

import com.iglo.exam.liber.book.Book;
import com.iglo.exam.liber.book.BookRepository;
import com.iglo.exam.liber.error.exception.ResourceNotFound;
import com.iglo.exam.liber.highlightBook.dto.HighlightBookInsertRequest;
import com.iglo.exam.liber.highlightBook.dto.HighlightBookResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class HighlightBookService {
    public final HighlightBookRepository highlightBookRepository;
    public final BookRepository bookRepository;
    public final HighlightBookDtoMapper highlightBookDtoMapper;

    public HighlightBookService(HighlightBookRepository highlightBookRepository, BookRepository bookRepository, HighlightBookDtoMapper highlightBookDtoMapper) {
        this.highlightBookRepository = highlightBookRepository;
        this.bookRepository = bookRepository;
        this.highlightBookDtoMapper = highlightBookDtoMapper;
    }


    public Page<HighlightBookResponse> findAllHighlightBooks(Pageable pageable) {
        return highlightBookRepository.findAll(pageable).map(highlightBookDtoMapper::toHighlightBookResponse);
    }

    public HighlightBookResponse findHighlightBookByOrderNumber(Integer orderNumber) {
        HighlightBook highlightBook = highlightBookRepository.findById(orderNumber)
                .orElseThrow(() -> new ResourceNotFound("Highlight book not found with order number: " + orderNumber));
        return highlightBookDtoMapper.toHighlightBookResponse(highlightBook);
    }

    @Transactional
    public HighlightBookResponse addHighlightBook(HighlightBookInsertRequest highlightBookInsertRequest) {
        Book book = bookRepository.findByIdAndIsDeletedFalse(highlightBookInsertRequest.getBookId()).orElseThrow(()->
                new ResourceNotFound("Book not found with id: " + highlightBookInsertRequest.getBookId()));

        highlightBookRepository.deleteByBookId(book.getId());

        HighlightBook highlightTarget = highlightBookRepository.findByOrderNumber(highlightBookInsertRequest.getOrderNumber())
                .orElseGet(HighlightBook::new);
        highlightTarget.setBook(book);
        highlightTarget.setOrderNumber(highlightBookInsertRequest.getOrderNumber());
        highlightTarget.setAddedDate(LocalDateTime.now());

        HighlightBook result = highlightBookRepository.save(highlightTarget);

        return highlightBookDtoMapper.toHighlightBookResponse(result);
    }

    @Transactional
    public void deleteHighlightBookByBookOrderNumber(Integer orderNumber) {
        if(highlightBookRepository.findByOrderNumber(orderNumber).isEmpty()){
            throw new ResourceNotFound("Highlight book not found with order number: " + orderNumber);
        }

        highlightBookRepository.deleteByOrderNumber(orderNumber);
    }
}
