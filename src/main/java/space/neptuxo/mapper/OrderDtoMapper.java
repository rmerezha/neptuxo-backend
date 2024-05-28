package space.neptuxo.mapper;

import space.neptuxo.dto.OrderDto;
import space.neptuxo.entity.Order;

public class OrderDtoMapper implements Mapper<OrderDto, Order> {

    @Override
    public Order map(OrderDto obj) {
        return Order.builder()
                .id(obj.id())
                .productId(obj.productId())
                .customerId(obj.customerId())
                .address(obj.address())
                .status(obj.status())
                .createdAt(obj.createdAt())
                .build();
    }

}
