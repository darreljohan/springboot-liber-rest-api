package com.iglo.exam.liber.author;

import com.iglo.exam.liber.author.dto.AuthorResponse;
import com.iglo.exam.liber.author.dto.AuthorUpsertRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring")
public interface AuthorDtoMapper {

    AuthorUpsertRequest toAuthorUpsertRequest(Author author);
    AuthorResponse toAuthorResponse(Author author);
    Author toAuthor(AuthorUpsertRequest authorUpsertRequest);
}
