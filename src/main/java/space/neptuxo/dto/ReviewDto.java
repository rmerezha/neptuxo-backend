package space.neptuxo.dto;

import lombok.Builder;

@Builder
public record ReviewDto(
        Long id,
        Long customerId,
        Long productId,
        String description,
        int rating
) {
}

