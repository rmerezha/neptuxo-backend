package space.neptuxo.dao;

import lombok.SneakyThrows;
import org.rmerezha.di.annotation.Inject;
import space.neptuxo.dto.ProductFilterDto;
import space.neptuxo.entity.Product;
import space.neptuxo.entity.ProductType;
import space.neptuxo.exception.SQLConstraintException;
import space.neptuxo.util.SQLQueryBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDao implements AbstractProductDao {

    @Inject
    private SQLQueryBuilder queryBuilder;

    private static final String FIND_BY_ID = """
            SELECT id,
                   created_by,
                   name,
                   description,
                   type,
                   price,
                   created_at,
                   image_path
            FROM product
            WHERE id = ?;
            """;
    private static final String SAVE = """
            INSERT INTO product (created_by, name, description, type, price, created_at, image_path)
            VALUES (?, ?, ?, ?, ?, ?, ?);
            """;
    private static final String REMOVE = """
            DELETE FROM product
            WHERE id = ?;
            """;
    private static final String UPDATE = """
            UPDATE product
            SET created_by = ?,
                name = ?,
                description = ?,
                type = ?,
                price = ?,
                created_at = ?,
                image_path = ?
            WHERE id = ?;
            """;
    private static final String FIND_BY_USER_ID = """
            SELECT id,
                   created_by,
                   name,
                   description,
                   type,
                   price,
                   created_at,
                   image_path
            FROM product
            WHERE created_by = ?;
            """;

    private static final String BASE_SELECT_FOR_SEARCH = """
            SELECT id,
                   created_by,
                   name,
                   description,
                   type,
                   price,
                   created_at,
                   image_path
            FROM product
            """;

    @Override
    @SneakyThrows
    public Optional<Product> findById(Long id, Connection connection) {
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
    public boolean remove(Long id, Connection connection) {
        try (var ps = connection.prepareStatement(REMOVE)) {
            ps.setLong(1, id);
            return ps.executeUpdate() != 0;
        }
    }

    @Override
    @SneakyThrows
    public boolean update(Product product, Connection connection) {
        try (var ps = connection.prepareStatement(UPDATE)) {
            ps.setLong(1, product.getCreatedBy());
            ps.setString(2, product.getName());
            ps.setString(3, product.getDescription());
            ps.setString(4, product.getType().name());
            ps.setInt(5, product.getPrice());
            ps.setTimestamp(6, Timestamp.valueOf(product.getCreatedAt()));
            ps.setString(7, product.getImagePath());
            ps.setLong(8, product.getId());
            return ps.executeUpdate() != 0;
        }
    }

    @Override
    public void save(Product product, Connection connection) {
        try (var ps = connection.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, product.getCreatedBy());
            ps.setString(2, product.getName());
            ps.setString(3, product.getDescription());
            ps.setString(4, product.getType().name());
            ps.setInt(5, product.getPrice());
            ps.setTimestamp(6, Timestamp.valueOf(product.getCreatedAt()));
            ps.setString(7, product.getImagePath());
            ps.executeUpdate();

            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                product.setId(generatedKeys.getLong("id"));
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
    public List<Product> findByUserId(long id, Connection connection) {
        try (var ps = connection.prepareStatement(FIND_BY_USER_ID)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            List<Product> products = new ArrayList<>();
            while (rs.next()) {
                products.add(buildProduct(rs));
            }
            return products;
        }
    }

    @Override
    @SneakyThrows
    public List<Product> findByFilter(ProductFilterDto filterDto, Connection connection) {
        String sql = queryBuilder.buildQuery(BASE_SELECT_FOR_SEARCH, filterDto);
        try (var ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            List<Product> products = new ArrayList<>();
            while (rs.next()) {
                products.add(buildProduct(rs));
            }
            return products;
        }
    }

    @SneakyThrows
    private Product buildProduct(ResultSet rs) {
        return Product.builder()
                .id(rs.getLong("id"))
                .createdBy(rs.getLong("created_by"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .type(ProductType.valueOf(rs.getString("type")))
                .price(rs.getInt("price"))
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .imagePath(rs.getString("image_path"))
                .build();
    }

}
