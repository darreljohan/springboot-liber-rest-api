package com.iglo.exam.liber.review;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class ReviewId {
    private Integer bookId;
    private Integer userId;
}
