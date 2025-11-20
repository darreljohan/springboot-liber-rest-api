package com.iglo.exam.liber.book;

import com.iglo.exam.liber.book.dto.BookResponse;
import com.iglo.exam.liber.book.dto.BookUpsertRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "Spring")
public interface BookDtoMapper {

    Book toBook(BookUpsertRequest bookUpsertRequest);

    @Mapping(source = "author.name", target = "authorName")
    BookResponse toBookResponse(Book book);

}
