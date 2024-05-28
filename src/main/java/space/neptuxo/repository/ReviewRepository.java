package space.neptuxo.repository;

import lombok.SneakyThrows;
import org.rmerezha.di.annotation.Inject;
import space.neptuxo.dao.AbstractOrderDao;
import space.neptuxo.dao.AbstractReviewDao;
import space.neptuxo.dto.ReviewDto;
import space.neptuxo.entity.Order;
import space.neptuxo.entity.OrderStatus;
import space.neptuxo.entity.Review;
import space.neptuxo.mapper.ReviewDtoMapper;
import space.neptuxo.mapper.ReviewMapper;
import space.neptuxo.util.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ReviewRepository implements AbstractReviewRepository {

    @Inject
    private AbstractReviewDao reviewDao;

    @Inject
    private AbstractOrderDao orderDao;

    @Inject
    private ConnectionPool connectionPool;

    @Inject
    private ReviewMapper reviewMapper;

    @Inject
    private ReviewDtoMapper reviewDtoMapper;

    @Override
    @SneakyThrows
    public List<ReviewDto> findByUserId(long userId) {
        try (var connection = connectionPool.get()) {
            return reviewDao.findByUserId(userId, connection)
                    .stream()
                    .map(reviewMapper::map)
                    .toList();
        }
    }

    @Override
    @SneakyThrows
    public List<ReviewDto> findByProductId(long productId) {
        try (var connection = connectionPool.get()) {
            return reviewDao.findByProductId(productId, connection)
                    .stream()
                    .map(reviewMapper::map)
                    .toList();
        }
    }

    @Override
    public boolean save(ReviewDto reviewDto) {
        Connection connection = null;
        try {
            connection = connectionPool.get();
            Review newReview = reviewDtoMapper.map(reviewDto);
            List<Order> userOrders = orderDao.findByUserId(reviewDto.customerId(), connection);
            for (var o : userOrders) {
                if (o.getProductId() == reviewDto.productId() && o.getStatus() == OrderStatus.COMPLETED) {
                    reviewDao.save(newReview, connection);
                    connection.commit();
                    return true;
                }
            }
            connection.commit();
            return false;
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            throw new RuntimeException();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
