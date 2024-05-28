package space.neptuxo.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rmerezha.di.model.BeanWithMocks;
import space.neptuxo.dao.AbstractOrderDao;
import space.neptuxo.dto.OrderDto;
import space.neptuxo.entity.Order;
import space.neptuxo.entity.OrderStatus;
import space.neptuxo.exception.SQLConstraintException;
import space.neptuxo.mapper.OrderDtoMapper;
import space.neptuxo.mapper.OrderMapper;
import space.neptuxo.util.ConnectionPool;
import space.neptuxo.util.DependencyInjector;

import java.sql.Connection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class OrderRepositoryTest {

    private OrderRepository repository;
    private AbstractOrderDao orderDao;
    private Connection connection;
    private OrderMapper orderMapper;
    private OrderDtoMapper orderDtoMapper;

    @BeforeEach
    void setUp() {
        BeanWithMocks<? extends OrderRepository> beanForTest = DependencyInjector.getBeanForTest(OrderRepository.class);
        repository = beanForTest.bean();
        orderDao = (AbstractOrderDao) beanForTest.mocks().get(AbstractOrderDao.class);
        orderMapper = (OrderMapper) beanForTest.mocks().get(OrderMapper.class);
        orderDtoMapper = (OrderDtoMapper) beanForTest.mocks().get(OrderDtoMapper.class);
        connection = mock(Connection.class);
        ConnectionPool connectionPool = (ConnectionPool) beanForTest.mocks().get(ConnectionPool.class);
        when(connectionPool.get()).thenReturn(connection);
    }

    @Test
    void findByUserId() {
        long userId = 111L;
        Order order = Order.builder()
                .status(OrderStatus.NEW)
                .customerId(userId)
                .productId(1)
                .build();
        OrderDto orderDto = OrderDto.builder()
                .status(OrderStatus.NEW)
                .customerId(userId)
                .productId(1)
                .build();
        List<OrderDto> expected = List.of(orderDto);
        when(orderDao.findByUserId(userId, connection)).thenReturn(List.of(order));
        when(orderMapper.map(order)).thenReturn(orderDto);

        assertEquals(expected, repository.findByUserId(userId));
    }

    @Test
    void create() {
        Order order = Order.builder()
                .status(OrderStatus.NEW)
                .customerId(111L)
                .productId(1)
                .build();
        OrderDto orderDto = OrderDto.builder()
                .status(OrderStatus.NEW)
                .customerId(111L)
                .productId(1)
                .build();

        when(orderDtoMapper.map(orderDto)).thenReturn(order);

        repository.create(orderDto);

        verify(orderDtoMapper).map(orderDto);
        verify(orderDao).save(any(Order.class), eq(connection));
    }

    @Test
    void createWithException() {
        Order order = Order.builder()
                .status(OrderStatus.NEW)
                .customerId(111L)
                .productId(1)
                .build();
        OrderDto orderDto = OrderDto.builder()
                .status(OrderStatus.NEW)
                .customerId(111L)
                .productId(1)
                .build();

        when(orderDtoMapper.map(orderDto)).thenReturn(order);
        doThrow(SQLConstraintException.class).when(orderDao).save(order, connection);

        assertThrows(SQLConstraintException.class, () -> repository.create(orderDto));
    }
}