package space.neptuxo.dao;

import space.neptuxo.entity.Order;

import java.sql.Connection;
import java.util.List;
import java.util.UUID;

public interface AbstractOrderDao extends Dao<Order, UUID> {

    List<Order> findByUserId(long userId, Connection connection);

    boolean hasActiveOrderByProductId(long productId, Connection connection);
}
