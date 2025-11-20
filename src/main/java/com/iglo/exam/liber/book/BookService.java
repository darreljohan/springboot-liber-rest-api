package com.iglo.exam.liber.book;

import com.iglo.exam.liber.author.Author;
import com.iglo.exam.liber.author.AuthorRepository;
import com.iglo.exam.liber.book.dto.BookResponse;
import com.iglo.exam.liber.book.dto.BookUpsertRequest;
import com.iglo.exam.liber.error.exception.ResourceNotFound;
import com.iglo.exam.liber.highlightBook.HighlightBookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    public final BookRepository bookRepository;
    public final HighlightBookRepository highlightBookRepository;
    public final AuthorRepository authorRepository;
    public final BookDtoMapper bookDtoMapper;

    public BookService(BookRepository bookRepository, HighlightBookRepository highlightBookRepository, AuthorRepository authorRepository, BookDtoMapper bookDtoMapper) {
        this.bookRepository = bookRepository;
        this.highlightBookRepository = highlightBookRepository;
        this.authorRepository = authorRepository;
        this.bookDtoMapper = bookDtoMapper;
    }

    public Page<BookResponse> findAllBooks(Pageable pageable) {
        return bookRepository.findByIsDeletedFalse(pageable)
                .map(bookDtoMapper::toBookResponse);
    }

    public Book findBookExistById(Integer id){
        return bookRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(()-> new ResourceNotFound("Book with id "+id+" is not exist"));
    }

    public BookResponse findBookById(Integer id){
        Book book = this.findBookExistById(id);
        return bookDtoMapper.toBookResponse(book);
    }

    public BookResponse addNewBook(BookUpsertRequest bookUpsertRequest){
        Author author = authorRepository.findById(bookUpsertRequest.getAuthorId())
                .orElseThrow(()-> new ResourceNotFound("Author with id "+bookUpsertRequest.getAuthorId()+" is not exist"));

        Book book = bookDtoMapper.toBook(bookUpsertRequest);
        book.setAuthor(author);
        Book savedBook = bookRepository.save(book);
        return bookDtoMapper.toBookResponse(savedBook);
    }

    public BookResponse updateBookById(Integer id, BookUpsertRequest bookUpsertRequest){
        Book existingBook = this.findBookExistById(id);

        Author author = authorRepository.findById(bookUpsertRequest.getAuthorId())
                .orElseThrow(()-> new ResourceNotFound("Author with id "+bookUpsertRequest.getAuthorId()+" is not exist"));

        Book book = bookDtoMapper.toBook(bookUpsertRequest);
        book.setId(existingBook.getId());
        book.setAuthor(author);
        Book updatedBook = bookRepository.save(book);
        return bookDtoMapper.toBookResponse(updatedBook);
    }

    public void deleteBookById(Integer id){
        Book existingBook = this.findBookExistById(id);
        existingBook.setDeleted(true);
        existingBook.setHighlightBook(null);
        bookRepository.save(existingBook);
    }
}
