package space.neptuxo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class Order {

    private UUID id;
    private long productId;
    private long customerId;
    private String address;
    private OrderStatus status;
    private LocalDateTime createdAt;

}
