package space.neptuxo.dao;

import lombok.SneakyThrows;
import space.neptuxo.entity.Review;
import space.neptuxo.exception.SQLConstraintException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReviewDao implements AbstractReviewDao {

    private static final String FIND_BY_ID = """
            SELECT id, customer_id, product_id, description, rating
            FROM review
            WHERE id = ?
            """;

    private static final String REMOVE = """
            DELETE FROM review
            WHERE id = ?
            """;

    private static final String SAVE = """
            INSERT INTO review (customer_id, product_id, description, rating)
            VALUES (?, ?, ?, ?)
            """;

    private static final String UPDATE = """
            UPDATE review
            SET customer_id = ?, product_id = ?, description = ?, rating = ?
            WHERE id = ?
            """;

    private final static String FIND_BY_USER_ID = """
            SELECT r.id,
                   r.customer_id,
                   r.product_id,
                   r.description,
                   r.rating
               FROM review r
               JOIN product p on p.id = r.product_id
               WHERE p.created_by = ?;
            """;

    private final static String FIND_BY_PRODUCT_ID = """
            SELECT id,
                   customer_id,
                   product_id,
                   description,
                   rating
               FROM review
               WHERE product_id = ?;
            """;

    @Override
    @SneakyThrows
    public Optional<Review> findById(Long id, Connection connection) {
        try (var ps = connection.prepareStatement(FIND_BY_ID)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            Review dbReview = null;
            if (rs.next()) {
                dbReview = buildReview(rs);
            }
            return Optional.ofNullable(dbReview);
        }
    }

    @Override
    @SneakyThrows
    public boolean remove(Long id, Connection connection) {
        try (var ps = connection.prepareStatement(REMOVE)) {
            ps.setLong(1, id);
            return ps.executeUpdate() != 0;
        }
    }

    @Override
    @SneakyThrows
    public boolean update(Review review, Connection connection) {
        try (var ps = connection.prepareStatement(UPDATE)) {
            ps.setLong(1, review.getCustomerId());
            ps.setLong(2, review.getProductId());
            ps.setString(3, review.getDescription());
            ps.setInt(4, review.getRating());
            ps.setLong(5, review.getId());
            return ps.executeUpdate() != 0;
        }
    }

    @Override
    public void save(Review review, Connection connection) {
        try (var ps = connection.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, review.getCustomerId());
            ps.setLong(2, review.getProductId());
            ps.setString(3, review.getDescription());
            ps.setInt(4, review.getRating());
            ps.executeUpdate();

            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                review.setId(generatedKeys.getLong("id"));
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new SQLConstraintException(e);
        }
    }

    @Override
    @SneakyThrows
    public List<Review> findByUserId(Long id, Connection connection) {
        try (var ps = connection.prepareStatement(FIND_BY_USER_ID)) {
            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();
            List<Review> reviews = new ArrayList<>();
            while (rs.next()) {
                reviews.add(buildReview(rs));
            }
            return reviews;
        }
    }

    @Override
    @SneakyThrows
    public List<Review> findByProductId(long productId, Connection connection) {
        try (var ps = connection.prepareStatement(FIND_BY_PRODUCT_ID)) {
            ps.setLong(1, productId);

            ResultSet rs = ps.executeQuery();
            List<Review> reviews = new ArrayList<>();
            while (rs.next()) {
                reviews.add(buildReview(rs));
            }

            return reviews;
        }
    }

    @SneakyThrows
    private Review buildReview(ResultSet rs) {
        return Review.builder()
                .id(rs.getLong("id"))
                .customerId(rs.getLong("customer_id"))
                .productId(rs.getLong("product_id"))
                .description(rs.getString("description"))
                .rating(rs.getInt("rating"))
                .build();
    }
}
