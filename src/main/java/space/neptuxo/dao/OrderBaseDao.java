package space.neptuxo.dao;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import space.neptuxo.entity.Order;
import space.neptuxo.entity.OrderStatus;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class OrderBaseDao implements Dao<Order, UUID> {

    private final Connection connection;

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


    @Override
    @SneakyThrows
    public Optional<Order> findById(UUID id) {
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
    public boolean remove(UUID id) {
        try (var ps = connection.prepareStatement(REMOVE)) {
            ps.setString(1, id.toString());
            return ps.executeUpdate() != 0;
        }
    }

    @Override
    @SneakyThrows
    public boolean update(Order obj) {
        try (var ps = connection.prepareStatement(UPDATE)) {
            ps.setLong(1, obj.getProductId());
            ps.setLong(2, obj.getCustomerId());
            ps.setString(3, obj.getAddress());
            ps.setString(4, obj.getStatus().name());
            ps.setTimestamp(5, Timestamp.valueOf(obj.getCreatedAt()));
            ps.setString(6, obj.getId().toString());
            return ps.executeUpdate() != 0;
        }

    }

    @Override
    @SneakyThrows
    public void save(Order obj) {
        try (var ps = connection.prepareStatement(SAVE)) {
            ps.setString(1, obj.getId().toString());
            ps.setLong(2, obj.getProductId());
            ps.setLong(3, obj.getCustomerId());
            ps.setString(4, obj.getAddress());
            ps.setString(5, obj.getStatus().name());
            ps.setTimestamp(6, Timestamp.valueOf(obj.getCreatedAt()));
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                obj.setId(UUID.fromString(generatedKeys.getString("id")));
            }
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
