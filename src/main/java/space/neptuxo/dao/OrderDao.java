package space.neptuxo.dao;

import lombok.SneakyThrows;
import space.neptuxo.entity.Order;
import space.neptuxo.entity.OrderStatus;
import space.neptuxo.exception.SQLConstraintException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class OrderDao implements AbstractOrderDao {

    private static final String SAVE = """
            INSERT INTO orders (id, product_id, customer_id, address, status, created_at)
            VALUES (?, ?, ?, ?, ?, ?);
            """;
    private static final String REMOVE = """
            DELETE FROM orders
            WHERE id = ?;
            """;
    private static final String UPDATE = """
            UPDATE orders
            SET product_id = ?,
                customer_id = ?,
                address = ?,
                status = ?,
                created_at = ?
            WHERE id = ?
            """;
    private static final String FIND_BY_ID = """
            SELECT id,
                   product_id,
                   customer_id,
                   address,
                   status,
                   created_at
            FROM orders
            WHERE id = ?;
            """;

    private static final String FIND_BY_USER_ID = """
            SELECT id,
                   product_id,
                   customer_id,
                   address,
                   status,
                   created_at
            FROM orders
            WHERE customer_id = ?;
            """;

    private static final String FIND_ACTIVE_ORDER_BY_PRODUCT_ID = """
            SELECT id
            FROM orders
            WHERE product_id = ? \s
            AND NOT status IN ('RETURNED', 'CANCELLED', 'COMPLETED')
            LIMIT 1;
            """;

    @Override
    @SneakyThrows
    public Optional<Order> findById(UUID id, Connection connection) {
        try (var ps = connection.prepareStatement(FIND_BY_ID)) {
            ps.setString(1, id.toString());
            ResultSet rs = ps.executeQuery();
            Order o = null;
            if (rs.next()) {
                o = buildOrder(rs);
            }
            return Optional.ofNullable(o);
        }
    }


    @Override
    @SneakyThrows
    public boolean remove(UUID id, Connection connection) {
        try (var ps = connection.prepareStatement(REMOVE)) {
            ps.setString(1, id.toString());
            return ps.executeUpdate() != 0;
        }
    }

    @Override
    @SneakyThrows
    public boolean update(Order order, Connection connection) {
        try (var ps = connection.prepareStatement(UPDATE)) {
            ps.setLong(1, order.getProductId());
            ps.setLong(2, order.getCustomerId());
            ps.setString(3, order.getAddress());
            ps.setString(4, order.getStatus().name());
            ps.setTimestamp(5, Timestamp.valueOf(order.getCreatedAt()));
            ps.setString(6, order.getId().toString());
            return ps.executeUpdate() != 0;
        }

    }

    @Override
    public void save(Order order, Connection connection) {
        try (var ps = connection.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, order.getId().toString());
            ps.setLong(2, order.getProductId());
            ps.setLong(3, order.getCustomerId());
            ps.setString(4, order.getAddress());
            ps.setString(5, order.getStatus().name());
            ps.setTimestamp(6, Timestamp.valueOf(order.getCreatedAt()));
            ps.executeUpdate();
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
    public List<Order> findByUserId(long userId, Connection connection) {
        try (var ps = connection.prepareStatement(FIND_BY_USER_ID)) {
            ps.setLong(1, userId);

            ResultSet rs = ps.executeQuery();

            List<Order> orders = new ArrayList<>();
            while (rs.next()) {
                orders.add(buildOrder(rs));
            }
            return orders;
        }
    }

    @Override
    @SneakyThrows
    public boolean hasActiveOrderByProductId(long productId, Connection connection) {
        try (var ps = connection.prepareStatement(FIND_ACTIVE_ORDER_BY_PRODUCT_ID)) {
            ps.setLong(1, productId);
            return ps.executeQuery().next();
        }
    }

    @SneakyThrows
    private Order buildOrder(ResultSet rs) {
        return Order.builder()
                .id(UUID.fromString(rs.getString("id")))
                .productId(rs.getLong("product_id"))
                .customerId(rs.getLong("customer_id"))
                .address(rs.getString("address"))
                .status(OrderStatus.valueOf(rs.getString("status")))
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .build();
    }

}
