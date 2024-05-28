package space.neptuxo.mapper;

import space.neptuxo.dto.OrderDto;
import space.neptuxo.entity.Order;

public class OrderMapper implements Mapper<Order, OrderDto> {

    @Override
    public OrderDto map(Order obj) {
        return OrderDto.builder()
                .id(obj.getId())
                .productId(obj.getProductId())
                .customerId(obj.getCustomerId())
                .address(obj.getAddress())
                .status(obj.getStatus())
                .createdAt(obj.getCreatedAt())
                .build();
    }

}
