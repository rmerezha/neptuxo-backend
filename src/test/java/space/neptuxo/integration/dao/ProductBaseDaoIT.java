package space.neptuxo.integration.dao;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import space.neptuxo.dao.ProductBaseDao;
import space.neptuxo.entity.Product;
import space.neptuxo.entity.ProductType;
import space.neptuxo.util.ConnectionPool;
import space.neptuxo.util_for_test.SqlInitializer;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProductBaseDaoIT {

    private ProductBaseDao productDao;
    private Connection connection;

    @BeforeEach
    public void init() {
        connection = ConnectionPool.get();
        SqlInitializer.insert(connection);
        productDao = new ProductBaseDao(connection);
    }

    @SneakyThrows
    @AfterEach
    public void clear() {
        SqlInitializer.clear(connection);
        connection.close();
    }

    @Test
    void findById() {

        long id = 5;

        Optional<Product> actual = productDao.findById(id);

        assertEquals(id, actual.map(Product::getId).orElseGet(Assertions::fail));

    }

    @Test
    void remove() {

        long id = 7;

        boolean result = productDao.remove(id);

        assertTrue(result);
        assertEquals(Optional.empty(), productDao.findById(id));

    }

    @Test
    void update() {

        Product product = Product.builder()
                .id(5L)
                .createdBy(2)
                .description("sss")
                .type(ProductType.ELECTRONICS)
                .count(5)
                .createdAt(LocalDateTime.now())
                .imagePath("///")
                .build();

        boolean result = productDao.update(product);

        assertTrue(result);
        assertEquals(product.getType(), productDao.findById(5L).get().getType());

    }

    @Test
    void save() {

        Product product = Product.builder()
                .createdBy(4)
                .description("test line")
                .type(ProductType.ELECTRONICS)
                .count(5)
                .createdAt(LocalDateTime.of(2020, Month.OCTOBER, 20, 23, 0))
                .imagePath("///")
                .build();

        productDao.save(product);

        Optional<Product> actual = productDao.findById(product.getId());
        assertNotEquals(0L, product.getId());
        assertTrue(actual.isPresent());
        assertEquals(product, actual.get());

    }
}