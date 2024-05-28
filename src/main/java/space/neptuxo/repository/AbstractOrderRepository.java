package space.neptuxo.repository;

import space.neptuxo.dto.OrderDto;

import java.util.List;

public interface AbstractOrderRepository {

    List<OrderDto> findByUserId(long userId);

    void create(OrderDto orderDto);
}
