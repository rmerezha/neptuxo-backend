package space.neptuxo.mapper;

import org.junit.jupiter.api.Test;
import space.neptuxo.dto.OrderDto;
import space.neptuxo.entity.Order;
import space.neptuxo.entity.OrderStatus;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderMapperTest {

    OrderMapper mapper = new OrderMapper();

    @Test
    void map() {
        OrderDto dto = OrderDto.builder()
                .id(UUID.fromString("a85a9a53-9256-4f11-bbd3-e8202a8f481a"))
                .productId(5)
                .customerId(5)
                .address("sss")
                .status(OrderStatus.NEW)
                .createdAt(LocalDateTime.now())
                .build();

        Order actual = mapper.map(dto);

        Order expected = Order.builder()
                .id(UUID.fromString("a85a9a53-9256-4f11-bbd3-e8202a8f481a"))
                .productId(5)
                .customerId(5)
                .address("sss")
                .status(OrderStatus.NEW)
                .createdAt(dto.createdAt())
                .build();
        assertEquals(expected, actual);
    }
}