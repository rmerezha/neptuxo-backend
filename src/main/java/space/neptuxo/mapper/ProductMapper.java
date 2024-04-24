package space.neptuxo.mapper;

import space.neptuxo.dto.ProductDto;
import space.neptuxo.entity.Product;

public class ProductMapper implements Map<Product, ProductDto> {

    @Override
    public ProductDto map(Product obj) {
        return ProductDto.builder()
                .id(obj.getId())
                .createdBy(obj.getCreatedBy())
                .description(obj.getDescription())
                .type(obj.getType())
                .count(obj.getCount())
                .createdAt(obj.getCreatedAt())
                .imagePath(obj.getImagePath())
                .build();
    }

}
