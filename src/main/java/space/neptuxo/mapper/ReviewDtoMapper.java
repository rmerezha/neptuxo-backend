package space.neptuxo.mapper;


import space.neptuxo.dto.ReviewDto;
import space.neptuxo.entity.Review;

public class ReviewDtoMapper implements Mapper<ReviewDto, Review> {

    @Override
    public Review map(ReviewDto dto) {
        return Review.builder()
                .id(dto.id())
                .customerId(dto.customerId())
                .productId(dto.productId())
                .description(dto.description())
                .rating(dto.rating())
                .build();
    }
}

