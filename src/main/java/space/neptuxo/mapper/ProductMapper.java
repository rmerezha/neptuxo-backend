package space.neptuxo.mapper;

import space.neptuxo.dto.ProductDto;
import space.neptuxo.entity.Product;

public class ProductMapper implements Mapper<Product, ProductDto> {

    @Override
    public ProductDto map(Product obj) {
        return ProductDto.builder()
                .id(obj.getId())
                .createdBy(obj.getCreatedBy())
                .name(obj.getName())
                .description(obj.getDescription())
                .type(obj.getType())
                .price(obj.getPrice())
                .createdAt(obj.getCreatedAt())
                .imagePath(obj.getImagePath())
                .build();
    }

}
