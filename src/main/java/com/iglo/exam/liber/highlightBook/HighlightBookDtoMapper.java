package com.iglo.exam.liber.highlightBook;

import com.iglo.exam.liber.book.BookDtoMapper;
import com.iglo.exam.liber.highlightBook.dto.HighlightBookResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "Spring", uses = {BookDtoMapper.class})
public interface HighlightBookDtoMapper {

    @Mapping(source = "book", target = "book")
    HighlightBookResponse toHighlightBookResponse(HighlightBook highlightBook);
}
