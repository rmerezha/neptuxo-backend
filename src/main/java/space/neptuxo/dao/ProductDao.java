package space.neptuxo.dao;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import space.neptuxo.entity.Product;
import space.neptuxo.entity.ProductType;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Optional;

@RequiredArgsConstructor
public class ProductDao implements Dao<Product, Long> {

    private final Connection connection;

    private static final String FIND_BY_ID = """
            SELECT id,
                   created_by,
                   description,
                   type,
                   count,
                   created_at,
                   image_path
            FROM product
            WHERE id = ?;
            """;

    private static final String SAVE = """
            INSERT INTO product (created_by, description, type, count, created_at,image_path)
            VALUES (?, ?, ?, ?, ?, ?);
            """;

    private static final String REMOVE = """
            DELETE FROM product
            WHERE id = ?;
            """;

    private static final String UPDATE = """
            UPDATE product
            SET created_by = ?,
                description = ?,
                type = ?,
                count = ?,
                created_at = ?,
                image_path = ?
            WHERE id = ?;
            """;

    @Override
    @SneakyThrows
    public Optional<Product> findById(Long id) {
        try (var ps = connection.prepareStatement(FIND_BY_ID)) {
            ps.setLong(1, id);
            ResultSet resultSet = ps.executeQuery();
            Product p = null;
            if (resultSet.next()) {
                p = buildProduct(resultSet);
            }
            return Optional.ofNullable(p);
        }
    }


    @Override
    @SneakyThrows
    public boolean remove(Long id) {
        try (var ps = connection.prepareStatement(REMOVE)) {
            ps.setLong(1, id);
            return ps.executeUpdate() != 0;
        }
    }

    @Override
    @SneakyThrows
    public boolean update(Product obj) {
        try (var ps = connection.prepareStatement(UPDATE)) {
            ps.setLong(1, obj.getCreatedBy());
            ps.setString(2, obj.getDescription());
            ps.setString(3, obj.getType().name());
            ps.setInt(4, obj.getCount());
            ps.setTimestamp(5, Timestamp.valueOf(obj.getCreatedAt()));
            ps.setString(6, obj.getImagePath());
            ps.setLong(7, obj.getId());
            return ps.executeUpdate() != 0;
        }
    }

    @Override
    @SneakyThrows
    public void save(Product obj) {
        try (var ps = connection.prepareStatement(SAVE)) {
            ps.setLong(1, obj.getCreatedBy());
            ps.setString(2, obj.getDescription());
            ps.setString(3, obj.getType().name());
            ps.setInt(4, obj.getCount());
            ps.setTimestamp(5, Timestamp.valueOf(obj.getCreatedAt()));
            ps.setString(6, obj.getImagePath());
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                obj.setId(generatedKeys.getLong("id"));
            }

        }
    }

    @SneakyThrows
    private Product buildProduct(ResultSet rs) {
        return Product.builder()
                .id(rs.getLong("id"))
                .createdBy(rs.getLong("created_by"))
                .description(rs.getString("description"))
                .type(ProductType.valueOf(rs.getString("type")))
                .count(rs.getInt("count"))
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .imagePath(rs.getString("image_path"))
                .build();

    }
}
