package space.neptuxo.dto;


import lombok.Builder;
import space.neptuxo.entity.ProductType;

import java.time.LocalDateTime;

@Builder
public record ProductDto(
        long id,
        long createdBy,
        String description,
        ProductType type,
        int count,
        LocalDateTime createdAt,
        String imagePath
) {
}
