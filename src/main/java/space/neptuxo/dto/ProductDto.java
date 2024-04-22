package space.neptuxo.dto;


import space.neptuxo.entity.ProductType;

import java.time.LocalDateTime;

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
