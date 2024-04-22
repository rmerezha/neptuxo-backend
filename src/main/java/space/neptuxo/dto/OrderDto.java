package space.neptuxo.dto;

import space.neptuxo.entity.OrderStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderDto(
        UUID id,
        long productId,
        long customer_id,
        String address,
        OrderStatus status,
        LocalDateTime createdAt
) {
}
