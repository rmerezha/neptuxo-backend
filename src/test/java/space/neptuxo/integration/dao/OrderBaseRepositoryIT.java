package space.neptuxo.integration.dao;

import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import space.neptuxo.dao.AbstractOrderRepository;
import space.neptuxo.entity.Order;
import space.neptuxo.entity.OrderStatus;
import space.neptuxo.util.ConnectionPool;
import space.neptuxo.util_for_test.SqlInitializer;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderBaseRepositoryIT {

    private AbstractOrderRepository orderDao;
    private Connection connection;

    private final UUID id = UUID.fromString("a85a9a53-9256-4f11-bbd3-e8202a8f481a");

    @BeforeEach
    public void init() {
        connection = ConnectionPool.get();
        SqlInitializer.insert(connection);
        orderDao = new AbstractOrderRepository(connection);
    }

    @SneakyThrows
    @AfterEach
    public void clear() {
        SqlInitializer.clear(connection);
        connection.close();
    }


    @Test
    void findById() {

        Optional<Order> actual = orderDao.findById(id);

        assertEquals(id, actual.map(Order::getId).orElseGet(Assertions::fail));

    }

    @Test
    void remove() {
        UUID uuid = UUID.fromString("bba55e36-b684-459b-bb98-411aa47d2dc0");

        boolean result = orderDao.remove(uuid);

        assertTrue(result);
        assertEquals(Optional.empty(), orderDao.findById(uuid));
    }

    @Test
    void update() {

        Order order = Order.builder()
                .id(id)
                .productId(2)
                .customerId(9)
                .address("Kiev")
                .status(OrderStatus.RETURNED)
                .createdAt(LocalDateTime.of(2022,2,2,2,2))
                .build();

        boolean result = orderDao.update(order);

        assertTrue(result);
        assertEquals(order, orderDao.findById(id).orElseGet(Assertions::fail));
    }

    @Test
    void save() {
    }
}