package space.neptuxo.mapper;

import org.junit.jupiter.api.Test;
import space.neptuxo.dto.OrderDto;
import space.neptuxo.entity.Order;
import space.neptuxo.entity.OrderStatus;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderMapperTest {

    private final OrderMapper mapper = new OrderMapper();

    @Test
    void map() {

        Order entity = Order.builder()
                .id(UUID.fromString("a85a9a53-9256-4f11-bbd3-e8202a8f481a"))
                .productId(5)
                .customerId(5)
                .address("sss")
                .status(OrderStatus.NEW)
                .createdAt(LocalDateTime.now())
                .build();

        OrderDto actual = mapper.map(entity);

        OrderDto expected = OrderDto.builder()
                .id(UUID.fromString("a85a9a53-9256-4f11-bbd3-e8202a8f481a"))
                .productId(5)
                .customerId(5)
                .address("sss")
                .status(OrderStatus.NEW)
                .createdAt(entity.getCreatedAt())
                .build();
        assertEquals(expected, actual);

    }
}