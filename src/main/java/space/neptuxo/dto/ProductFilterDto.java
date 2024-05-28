package space.neptuxo.dto;

import lombok.Builder;
import space.neptuxo.entity.ProductType;

import java.util.List;

@Builder
public record ProductFilterDto (
        Integer limit,
        List<ProductType> types,
        Integer priceFrom,
        Integer priceTo,
        Integer lastIndex,
        String searchQuery
) {
}
