package space.neptuxo.repository;

import space.neptuxo.dto.ReviewDto;

import java.util.List;

public interface AbstractReviewRepository {

    List<ReviewDto> findByUserId(long id);

    List<ReviewDto> findByProductId(long productId);

    boolean save(ReviewDto reviewDto);
}
