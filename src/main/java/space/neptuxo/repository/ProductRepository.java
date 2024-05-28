package space.neptuxo.repository;

import lombok.SneakyThrows;
import org.rmerezha.di.annotation.Inject;
import space.neptuxo.dao.AbstractOrderDao;
import space.neptuxo.dao.AbstractProductDao;
import space.neptuxo.dto.ProductDto;
import space.neptuxo.dto.ProductFilterDto;
import space.neptuxo.entity.Product;
import space.neptuxo.exception.ActiveOrderExistsException;
import space.neptuxo.mapper.ProductDtoMapper;
import space.neptuxo.mapper.ProductMapper;
import space.neptuxo.util.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ProductRepository implements AbstractProductRepository {

    @Inject
    private AbstractProductDao productDao;

    @Inject
    private AbstractOrderDao orderDao;

    @Inject
    private ProductDtoMapper productDtoMapper;

    @Inject
    private ProductMapper productMapper;

    @Inject
    private ConnectionPool connectionPool;


    @Override
    public void create(ProductDto productDto) {
        try (var connection = connectionPool.get()) {
            Product product = productDtoMapper.map(productDto);
            product.setCreatedAt(LocalDateTime.now());
            productDao.save(product, connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public boolean delete(long userId, long productId) {
        Connection connection = null;
        try {
            connection = connectionPool.get();
            connection.setAutoCommit(false);
            List<Product> userProducts = productDao.findByUserId(userId, connection);

            Optional<Product> optionalProduct = userProducts.stream()
                    .filter(p -> p.getId() == productId)
                    .findFirst();

            if (optionalProduct.isEmpty()) {
                connection.commit();
                return false;
            }

            if (orderDao.hasActiveOrderByProductId(productId, connection)) {
                connection.rollback();
                throw new ActiveOrderExistsException();
            }
            boolean isRemoved = productDao.remove(productId, connection);
            connection.commit();
            return isRemoved;
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

    @Override
    @SneakyThrows
    public List<ProductDto> findByFilter(ProductFilterDto filterDto) {
        try (var connection = connectionPool.get()) {
            return productDao.findByFilter(filterDto, connection)
                    .stream()
                    .map(productMapper::map)
                    .toList();
        }
    }
}
