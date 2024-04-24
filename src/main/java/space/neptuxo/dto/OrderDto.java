package space.neptuxo.dto;

import lombok.Builder;
import space.neptuxo.entity.OrderStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record OrderDto(
        UUID id,
        long productId,
        long customerId,
        String address,
        OrderStatus status,
        LocalDateTime createdAt
) {
}
