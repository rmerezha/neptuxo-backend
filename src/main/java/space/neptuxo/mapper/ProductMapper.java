package space.neptuxo.mapper;


import space.neptuxo.dto.ProductDto;
import space.neptuxo.entity.Product;

public class ProductMapper implements Map<ProductDto, Product>{
    @Override
    public Product map(ProductDto obj) {
        return Product.builder()
                .id(obj.id())
                .createdBy(obj.createdBy())
                .description(obj.description())
                .type(obj.type())
                .count(obj.count())
                .createdAt(obj.createdAt())
                .imagePath(obj.imagePath())
                .build();
    }
}
