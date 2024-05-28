package space.neptuxo.mapper;

import org.junit.jupiter.api.Test;
import space.neptuxo.dto.ReviewDto;
import space.neptuxo.entity.Review;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReviewMapperTest {

    private final ReviewMapper reviewMapper = new ReviewMapper();


    @Test
    public void map() {
        Review review = Review.builder()
                .id(1L)
                .customerId(200L)
                .productId(300L)
                .description("Test Review")
                .rating(5)
                .build();

        ReviewDto actual = reviewMapper.map(review);

        ReviewDto expected = new ReviewDto(
                1L,
                200L,
                300L,
                "Test Review",
                5
        );

        assertEquals(expected, actual);
    }

}