package space.neptuxo.dto;


import lombok.Builder;
import space.neptuxo.entity.ProductType;

import java.time.LocalDateTime;

@Builder
public record ProductDto(
        long id,
        long createdBy,
        String name,
        String description,
        ProductType type,
        int price,
        LocalDateTime createdAt,
        String imagePath
) {
}
