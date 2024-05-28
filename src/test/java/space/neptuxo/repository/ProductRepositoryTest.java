package space.neptuxo.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rmerezha.di.model.BeanWithMocks;
import space.neptuxo.dao.AbstractOrderDao;
import space.neptuxo.dao.AbstractProductDao;
import space.neptuxo.dto.ProductDto;
import space.neptuxo.dto.ProductFilterDto;
import space.neptuxo.entity.Product;
import space.neptuxo.exception.ActiveOrderExistsException;
import space.neptuxo.exception.SQLConstraintException;
import space.neptuxo.mapper.ProductDtoMapper;
import space.neptuxo.mapper.ProductMapper;
import space.neptuxo.util.ConnectionPool;
import space.neptuxo.util.DependencyInjector;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ProductRepositoryTest {

    private AbstractProductRepository repository;
    private AbstractProductDao productDao;
    private AbstractOrderDao orderDao;
    private ProductDtoMapper productDtoMapper;
    private ProductMapper productMapper;
    private Connection connection;

    @BeforeEach
    void setUp() {
        BeanWithMocks<? extends AbstractProductRepository> beanForTest = DependencyInjector.getBeanForTest(AbstractProductRepository.class);
        repository = beanForTest.bean();
        orderDao = (AbstractOrderDao) beanForTest.mocks().get(AbstractOrderDao.class);
        productDao = (AbstractProductDao) beanForTest.mocks().get(AbstractProductDao.class);
        productMapper = (ProductMapper) beanForTest.mocks().get(ProductMapper.class);
        productDtoMapper = (ProductDtoMapper) beanForTest.mocks().get(ProductDtoMapper.class);
        connection = mock(Connection.class);
        ConnectionPool connectionPool = (ConnectionPool) beanForTest.mocks().get(ConnectionPool.class);
        when(connectionPool.get()).thenReturn(connection);
    }

    @Test
    void create() {
        Product product = Product.builder()
                .name("name")
                .price(123)
                .createdBy(1)
                .createdAt(LocalDateTime.MIN)
                .build();
        ProductDto productDto = ProductDto.builder()
                .name("name")
                .price(123)
                .createdBy(1)
                .createdAt(LocalDateTime.MIN)
                .build();

        when(productDtoMapper.map(productDto)).thenReturn(product);

        repository.create(productDto);

        verify(productDtoMapper).map(productDto);
        verify(productDao).save(any(Product.class), eq(connection));
    }

    @Test
    void createWithException() {
        Product product = Product.builder()
                .name("name")
                .price(123)
                .createdBy(1)
                .createdAt(LocalDateTime.MIN)
                .build();
        ProductDto productDto = ProductDto.builder()
                .name("name")
                .price(123)
                .createdBy(1)
                .createdAt(LocalDateTime.MIN)
                .build();

        when(productDtoMapper.map(productDto)).thenReturn(product);
        doThrow(SQLConstraintException.class).when(productDao).save(product, connection);

        assertThrows(SQLConstraintException.class, () -> repository.create(productDto));
    }

    @Test
    void findByFilter() {
        Product product = Product.builder()
                .name("name")
                .price(123)
                .createdBy(1)
                .createdAt(LocalDateTime.MIN)
                .build();
        ProductDto productDto = ProductDto.builder()
                .name("name")
                .price(123)
                .createdBy(1)
                .createdAt(LocalDateTime.MIN)
                .build();
        ProductFilterDto filterDto = ProductFilterDto.builder()
                .limit(20)
                .build();

        when(productDao.findByFilter(filterDto, connection)).thenReturn(List.of(product));
        when(productMapper.map(product)).thenReturn(productDto);

        assertEquals(List.of(productDto), repository.findByFilter(filterDto));
    }

    @Test
    void deleteSuccess() throws SQLException {
        long userId = 111L;
        long productId = 1L;
        Product product = Product.builder()
                .id(productId)
                .name("name")
                .price(123)
                .createdBy(1)
                .createdAt(LocalDateTime.MIN)
                .build();
        List<Product> userProducts = List.of(product);

        when(productDao.findByUserId(userId, connection)).thenReturn(userProducts);
        when(orderDao.hasActiveOrderByProductId(productId, connection)).thenReturn(false);
        when(productDao.remove(productId, connection)).thenReturn(true);

        assertTrue(repository.delete(userId, productId));

        verify(connection).setAutoCommit(false);
        verify(connection).commit();
        verify(connection).close();
    }

    @Test
    void deleteProductNotFound() throws SQLException {
        long userId = 111L;
        long productId = 1L;
        List<Product> userProducts = List.of();

        when(productDao.findByUserId(userId, connection)).thenReturn(userProducts);

        assertFalse(repository.delete(userId, productId));

        verify(connection).setAutoCommit(false);
        verify(connection).commit();
        verify(connection).close();
    }

    @Test
    void deleteWithActiveOrder() throws SQLException {
        long userId = 111L;
        long productId = 1L;
        Product product = Product.builder()
                .id(productId)
                .name("name")
                .price(123)
                .createdBy(1)
                .createdAt(LocalDateTime.MIN)
                .build();
        List<Product> userProducts = List.of(product);

        when(productDao.findByUserId(userId, connection)).thenReturn(userProducts);
        when(orderDao.hasActiveOrderByProductId(productId, connection)).thenReturn(true);

        assertThrows(ActiveOrderExistsException.class, () -> repository.delete(userId, productId));

        verify(connection).setAutoCommit(false);
        verify(connection).rollback();
        verify(connection).close();
    }
}