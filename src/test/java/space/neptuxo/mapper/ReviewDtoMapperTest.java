package space.neptuxo.mapper;

import org.junit.jupiter.api.Test;
import space.neptuxo.dto.ReviewDto;
import space.neptuxo.entity.Review;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReviewDtoMapperTest {


    private final ReviewDtoMapper reviewDtoMapper = new ReviewDtoMapper();

    @Test
    void map() {
        ReviewDto reviewDto = new ReviewDto(
                1L,
                200L,
                300L,
                "Test Review",
                5
        );


        Review actual = reviewDtoMapper.map(reviewDto);

        Review expected = Review.builder()
                .id(1L)
                .customerId(200L)
                .productId(300L)
                .description("Test Review")
                .rating(5)
                .build();

        assertEquals(expected, actual);
    }
}