package space.neptuxo.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rmerezha.di.model.BeanWithMocks;
import space.neptuxo.dao.AbstractOrderDao;
import space.neptuxo.dao.AbstractReviewDao;
import space.neptuxo.dto.ReviewDto;
import space.neptuxo.entity.Order;
import space.neptuxo.entity.OrderStatus;
import space.neptuxo.entity.Review;
import space.neptuxo.exception.SQLConstraintException;
import space.neptuxo.mapper.ReviewDtoMapper;
import space.neptuxo.mapper.ReviewMapper;
import space.neptuxo.util.ConnectionPool;
import space.neptuxo.util.DependencyInjector;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewRepositoryTest {

    private AbstractReviewRepository repository;
    private AbstractReviewDao reviewDao;
    private AbstractOrderDao orderDao;
    private Connection connection;
    private ReviewMapper reviewMapper;
    private ReviewDtoMapper reviewDtoMapper;

    @BeforeEach
    void setUp() {
        BeanWithMocks<? extends AbstractReviewRepository> beanForTest = DependencyInjector.getBeanForTest(AbstractReviewRepository.class);
        repository = beanForTest.bean();
        orderDao = (AbstractOrderDao) beanForTest.mocks().get(AbstractOrderDao.class);
        reviewDao = (AbstractReviewDao) beanForTest.mocks().get(AbstractReviewDao.class);
        reviewMapper = (ReviewMapper) beanForTest.mocks().get(ReviewMapper.class);
        reviewDtoMapper = (ReviewDtoMapper) beanForTest.mocks().get(ReviewDtoMapper.class);
        connection = mock(Connection.class);
        ConnectionPool connectionPool = (ConnectionPool) beanForTest.mocks().get(ConnectionPool.class);
        when(connectionPool.get()).thenReturn(connection);
    }

    @Test
    void findByUserId() {
        long userId = 111L;
        Review review = Review.builder()
                .id(1L)
                .customerId(userId)
                .description("text")
                .build();
        ReviewDto reviewDto = ReviewDto.builder()
                .id(1L)
                .customerId(userId)
                .description("text")
                .build();
        when(reviewDao.findByUserId(userId, connection)).thenReturn(List.of(review));
        when(reviewMapper.map(review)).thenReturn(reviewDto);

        assertEquals(List.of(reviewDto), repository.findByUserId(userId));
    }

    @Test
    void findByProductId() {
        long productId = 111L;
        Review review = Review.builder()
                .id(1L)
                .customerId(productId)
                .description("text")
                .build();
        ReviewDto reviewDto = ReviewDto.builder()
                .id(1L)
                .customerId(productId)
                .description("text")
                .build();
        when(reviewDao.findByProductId(productId, connection)).thenReturn(List.of(review));
        when(reviewMapper.map(review)).thenReturn(reviewDto);

        assertEquals(List.of(reviewDto), repository.findByProductId(productId));
    }


    @Test
    void saveSuccess() throws SQLException {
        Review review = Review.builder()
                .id(1L)
                .customerId(111L)
                .productId(111L)
                .description("text")
                .build();
        ReviewDto reviewDto = ReviewDto.builder()
                .id(1L)
                .customerId(111L)
                .productId(111L)
                .description("text")
                .build();

        when(reviewDtoMapper.map(reviewDto)).thenReturn(review);
        when(orderDao.findByUserId(111L, connection)).thenReturn(List.of(Order.builder().id(UUID.randomUUID()).productId(111L).status(OrderStatus.COMPLETED).build()));

        assertTrue(repository.save(reviewDto));

        verify(orderDao).findByUserId(111L, connection);
        verify(connection).commit();
        verify(reviewDtoMapper).map(reviewDto);
        verify(reviewDao).save(review, connection);
    }

    @Test
    void saveFailureNoOrder() throws SQLException {
        Review review = Review.builder()
                .id(1L)
                .customerId(111L)
                .description("text")
                .build();
        ReviewDto reviewDto = ReviewDto.builder()
                .id(1L)
                .customerId(111L)
                .description("text")
                .build();

        when(reviewDtoMapper.map(reviewDto)).thenReturn(review);
        when(orderDao.findByUserId(111L, connection)).thenReturn(Collections.emptyList());

        assertFalse(repository.save(reviewDto));

        verify(orderDao).findByUserId(111L, connection);
        verify(connection).commit();
    }

    @Test
    void saveFailureOrderNotCompleted() throws SQLException {
        Review review = Review.builder()
                .id(1L)
                .customerId(111L)
                .productId(111L)
                .description("text")
                .build();
        ReviewDto reviewDto = ReviewDto.builder()
                .id(1L)
                .customerId(111L)
                .productId(111L)
                .description("text")
                .build();

        when(reviewDtoMapper.map(reviewDto)).thenReturn(review);
        when(orderDao.findByUserId(111L, connection)).thenReturn(List.of(Order.builder().id(UUID.randomUUID()).productId(111L).status(OrderStatus.NEW).build()));

        assertFalse(repository.save(reviewDto));

        verify(orderDao).findByUserId(111L, connection);
        verify(connection).commit();
    }

    @Test
    void saveWithException() throws SQLException {
        Review review = Review.builder()
                .id(1L)
                .customerId(111L)
                .productId(111L)
                .description("text")
                .build();
        ReviewDto reviewDto = ReviewDto.builder()
                .id(1L)
                .customerId(111L)
                .productId(111L)
                .description("text")
                .build();

        when(reviewDtoMapper.map(reviewDto)).thenReturn(review);
        when(orderDao.findByUserId(111L, connection)).thenReturn(List.of(Order.builder().id(UUID.randomUUID()).productId(111L).status(OrderStatus.COMPLETED).build()));
        doThrow(SQLConstraintException.class).when(reviewDao).save(review, connection);

        assertThrows(SQLConstraintException.class, () -> repository.save(reviewDto));

        verify(orderDao).findByUserId(111L, connection);
    }
}