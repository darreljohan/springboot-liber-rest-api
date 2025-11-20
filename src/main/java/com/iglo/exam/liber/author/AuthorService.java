package com.iglo.exam.liber.author;

import com.iglo.exam.liber.author.dto.AuthorResponse;
import com.iglo.exam.liber.author.dto.AuthorUpsertRequest;
import com.iglo.exam.liber.book.Book;
import com.iglo.exam.liber.book.BookRepository;
import com.iglo.exam.liber.error.exception.DeletionConflict;
import com.iglo.exam.liber.error.exception.DuplicateNameException;
import com.iglo.exam.liber.error.exception.ResourceNotFound;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {

    private final AuthorDtoMapper authorDtoMapper;
    private  final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public AuthorService(AuthorDtoMapper authorDtoMapper, AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorDtoMapper = authorDtoMapper;
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    public Page<AuthorResponse> findALlAuthor(Pageable pageable){
        Page<Author> authors = authorRepository.findAll(pageable);
        return authors.map(authorDtoMapper::toAuthorResponse);
    }

    public Author findAuthorExistById(Integer id){
        return authorRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFound("Author with id "+id+" is not exist"));
    }

    public AuthorResponse findAuthorById(Integer id){
        Author author = this.findAuthorExistById(id);
        return authorDtoMapper.toAuthorResponse(author);
    }

    public boolean isAuthorExistByName(String name){
        return authorRepository.existsByName(name);
    }

    public AuthorResponse addNewAuthor(AuthorUpsertRequest authorUpsertRequest){
        if(isAuthorExistByName(authorUpsertRequest.getName())){
            throw new DuplicateNameException("Author with name "+authorUpsertRequest.getName()+" is already exist");
        }

        Author savedAuthor = authorRepository.save(authorDtoMapper.toAuthor(authorUpsertRequest));
        return authorDtoMapper.toAuthorResponse(savedAuthor);
    }

    public AuthorResponse updateAuthorById(Integer id, AuthorUpsertRequest authorUpsertRequest){
        Author existingAuthor = this.findAuthorExistById(id);

        if(!existingAuthor.getName().equals(authorUpsertRequest.getName())){
            if(isAuthorExistByName(authorUpsertRequest.getName())){
                throw new DuplicateNameException("Author with name "+authorUpsertRequest.getName()+" is already exist");
            }
        }

        Author author = authorDtoMapper.toAuthor(authorUpsertRequest);
        author.setId(id);

        Author updatedAuthor = authorRepository.save(author);
        return authorDtoMapper.toAuthorResponse(updatedAuthor);
    }

    public void deleteAuthorById(Integer id){
        Author author = this.findAuthorExistById(id);
        if(!author.getBooks().isEmpty()){
            throw new DeletionConflict("Author with id "+id+" already had book written");
        }

        List<Book> booksByAuthor = bookRepository.findByAuthor_IdAndIsDeletedFalse(author.getId());
        if(!booksByAuthor.isEmpty()){
            StringBuilder bookTitles = new StringBuilder();
            for(Book book : booksByAuthor){
                bookTitles.append(book.getName()).append(", ");
            }
            throw new DeletionConflict("Author with id "+id+" already had book written :"+bookTitles);
        }

        authorRepository.delete(author);
    }
}
