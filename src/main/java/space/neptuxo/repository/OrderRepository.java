package space.neptuxo.repository;

import lombok.SneakyThrows;
import org.rmerezha.di.annotation.Inject;
import space.neptuxo.dao.AbstractOrderDao;
import space.neptuxo.dto.OrderDto;
import space.neptuxo.entity.Order;
import space.neptuxo.entity.OrderStatus;
import space.neptuxo.mapper.OrderDtoMapper;
import space.neptuxo.mapper.OrderMapper;
import space.neptuxo.util.ConnectionPool;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class OrderRepository implements AbstractOrderRepository {

    @Inject
    private AbstractOrderDao orderDao;

    @Inject
    private ConnectionPool connectionPool;

    @Inject
    private OrderMapper orderMapper;

    @Inject
    private OrderDtoMapper orderDtoMapper;

    @Override
    @SneakyThrows
    public List<OrderDto> findByUserId(long userId) {
        try (var connection = connectionPool.get()) {
            return orderDao.findByUserId(userId, connection)
                    .stream()
                    .map(orderMapper::map)
                    .toList();
        }
    }

    @Override
    public void create(OrderDto orderDto) {
        try (var connection = connectionPool.get()) {
            Order newOrder = orderDtoMapper.map(orderDto);
            newOrder.setId(UUID.randomUUID());
            newOrder.setCreatedAt(LocalDateTime.now());
            newOrder.setStatus(OrderStatus.NEW);
            orderDao.save(newOrder, connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
