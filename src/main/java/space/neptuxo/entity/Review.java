package space.neptuxo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Review {

    private Long id;
    private Long customerId;
    private Long productId;
    private String description;
    private int rating;

}
