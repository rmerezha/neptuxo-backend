package space.neptuxo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rmerezha.di.model.BeanWithMocks;
import space.neptuxo.dto.ReviewDto;
import space.neptuxo.repository.AbstractReviewRepository;
import space.neptuxo.util.DependencyInjector;
import space.neptuxo.util.JsonBuilder;
import space.neptuxo.util.JsonBuilderFactory;
import space.neptuxo.util.JsonParser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ReviewServiceTest {

    private ReviewService service;
    private AbstractReviewRepository repository;
    private JsonParser jsonParser;
    private JsonBuilder jsonBuilder;

    @BeforeEach
    void setUp() {
        BeanWithMocks<ReviewService> beanForTest = DependencyInjector.getBeanForTest(ReviewService.class);
        service = beanForTest.bean();
        repository = (AbstractReviewRepository) beanForTest.mocks().get(AbstractReviewRepository.class);
        JsonBuilderFactory jsonBuilderFactory = (JsonBuilderFactory) beanForTest.mocks().get(JsonBuilderFactory.class);
        jsonBuilder = mock(JsonBuilder.class);
        jsonParser = (JsonParser) beanForTest.mocks().get(JsonParser.class);
        doReturn(jsonBuilder).when(jsonBuilderFactory).create();
    }

    @Test
    void findByUserId() {
        long userId = 100;
        List<ReviewDto> reviews = List.of(ReviewDto.builder()
                .id(11L)
                .productId(12345L)
                .customerId(userId)
                .rating(5)
                .description("Great product!")
                .build());
        String expected = "{\"status\":\"success\",\"data\":{\"reviews\":[{\"id\":11,\"productId\":12345,\"customerId\":100,\"rating\":5,\"description\":\"Great product!\"}]}}";

        when(repository.findByUserId(userId)).thenReturn(reviews);
        when(jsonBuilder.setStatus(any())).thenReturn(jsonBuilder);
        when(jsonBuilder.setData(anyString(), any(List.class))).thenReturn(jsonBuilder);
        when(jsonBuilder.build()).thenReturn(expected);

        assertEquals(expected, service.findByUserId(userId));
    }

    @Test
    void findByProductId() {
        long productId = 12345;
        List<ReviewDto> reviews = List.of(ReviewDto.builder()
                .id(11L)
                .productId(productId)
                .customerId(100L)
                .rating(4)
                .description("Good product!")
                .build());
        String expected = "{\"status\":\"success\",\"data\":{\"reviews\":[{\"id\":11,\"productId\":12345,\"customerId\":100,\"rating\":4,\"description\":\"Good product!\"}]}}";

        when(repository.findByProductId(productId)).thenReturn(reviews);
        when(jsonBuilder.setStatus(any())).thenReturn(jsonBuilder);
        when(jsonBuilder.setData(anyString(), any(List.class))).thenReturn(jsonBuilder);
        when(jsonBuilder.build()).thenReturn(expected);

        assertEquals(expected, service.findByProductId(productId));
    }

    @Test
    void createSuccess() {
        String json = """
        {
          "productId": 12345,
          "customerId": 67890,
          "rating": 5,
          "description": "Excellent product!"
        }
        """;
        ReviewDto review = ReviewDto.builder()
                .productId(12345L)
                .customerId(67890L)
                .rating(5)
                .description("Excellent product!")
                .build();
        String expected = "{\"status\":\"success\"}";

        when(jsonParser.parse(any(InputStream.class), eq(ReviewDto.class))).thenReturn(review);
        when(repository.save(review)).thenReturn(true);
        when(jsonBuilder.setStatus(any())).thenReturn(jsonBuilder);
        when(jsonBuilder.build()).thenReturn(expected);

        assertEquals(expected, service.create(new ByteArrayInputStream(json.getBytes())));
    }

    @Test
    void createFail() {
        String json = """
        {
          "productId": 12345,
          "customerId": 67890,
          "rating": 5,
          "comment": "Excellent product!"
        }
        """;
        ReviewDto review = ReviewDto.builder()
                .id(11L)
                .productId(12345L)
                .customerId(67890L)
                .rating(5)
                .description("Excellent product!")
                .build();
        String expected = "{\"status\":\"fail\",\"errors\":[{\"code\":10,\"message\":\"Order is not completed\"}]}";

        when(jsonParser.parse(any(InputStream.class), eq(ReviewDto.class))).thenReturn(review);
        when(repository.save(review)).thenReturn(false);
        when(jsonBuilder.setStatus(any())).thenReturn(jsonBuilder);
        when(jsonBuilder.setErrors(any())).thenReturn(jsonBuilder);
        when(jsonBuilder.build()).thenReturn(expected);

        assertEquals(expected, service.create(new ByteArrayInputStream(json.getBytes())));
    }



}