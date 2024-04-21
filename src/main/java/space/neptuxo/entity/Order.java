package space.neptuxo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class Order {

    private UUID id;
    private long productId;
    private long userId;
    private String address;
    private OrderStatus status;

}
