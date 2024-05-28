package space.neptuxo.dao;

import space.neptuxo.entity.Review;

import java.sql.Connection;
import java.util.List;

public interface AbstractReviewDao extends Dao<Review, Long> {

    List<Review> findByUserId(Long id, Connection connection);

    List<Review> findByProductId(long productId, Connection connection);
}
