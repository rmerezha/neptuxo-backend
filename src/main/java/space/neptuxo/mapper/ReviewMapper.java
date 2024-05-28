package space.neptuxo.mapper;

import space.neptuxo.dto.ReviewDto;
import space.neptuxo.entity.Review;

public class ReviewMapper implements Mapper<Review, ReviewDto> {

    @Override
    public ReviewDto map(Review obj) {
        return new ReviewDto(
                obj.getId(),
                obj.getCustomerId(),
                obj.getProductId(),
                obj.getDescription(),
                obj.getRating()
        );
    }
}
