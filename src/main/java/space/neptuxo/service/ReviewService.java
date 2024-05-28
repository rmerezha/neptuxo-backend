package space.neptuxo.service;

import org.rmerezha.di.annotation.Inject;
import space.neptuxo.dto.ReviewDto;
import space.neptuxo.repository.AbstractReviewRepository;
import space.neptuxo.util.Error;
import space.neptuxo.util.*;

import java.io.InputStream;
import java.util.List;

public class ReviewService {

    @Inject
    private AbstractReviewRepository repository;

    @Inject
    private JsonBuilderFactory jsonBuilderFactory;

    @Inject
    private JsonParser jsonParser;


    public String findByUserId(long userId) {
        List<ReviewDto> reviews = repository.findByUserId(userId);

        return buildReviewsJson(reviews);
    }

    public String findByProductId(long productId) {
        List<ReviewDto> reviews = repository.findByProductId(productId);

        return buildReviewsJson(reviews);
    }

    public String create(InputStream reqJson) {
        JsonBuilder jsonBuilder = jsonBuilderFactory.create();

        ReviewDto reviewDto = jsonParser.parse(reqJson, ReviewDto.class);

        if (repository.save(reviewDto)) {
            return jsonBuilder.setStatus(Status.SUCCESS).build();
        }
        return jsonBuilder.setStatus(Status.FAIL)
                .setErrors(List.of(Error.ORDER_NOT_COMPLETED))
                .build();

    }

    private String buildReviewsJson(List<ReviewDto> reviews) {
        String reviewsKey = "reviews";
        return jsonBuilderFactory.create()
                .setStatus(Status.SUCCESS)
                .setData(reviewsKey, reviews)
                .build();
    }
}
