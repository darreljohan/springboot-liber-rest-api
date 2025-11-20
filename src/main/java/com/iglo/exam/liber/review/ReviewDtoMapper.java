package com.iglo.exam.liber.review;

import com.iglo.exam.liber.book.Book;
import com.iglo.exam.liber.book.BookDtoMapper;
import com.iglo.exam.liber.review.dto.ReviewResponse;
import com.iglo.exam.liber.user.User;
import com.iglo.exam.liber.user.UserDtoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "Spring", uses = {BookDtoMapper.class, UserDtoMapper.class})
public interface ReviewDtoMapper {

    @Mapping(source =  "finishedReadingDate", target = "finishedReadingDate")
    ReviewResponse toReviewResponse(Review review);
}
