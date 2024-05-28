package space.neptuxo.mapper;


import space.neptuxo.dto.ProductDto;
import space.neptuxo.entity.Product;

public class ProductDtoMapper implements Mapper<ProductDto, Product> {
    @Override
    public Product map(ProductDto obj) {
        return Product.builder()
                .id(obj.id())
                .createdBy(obj.createdBy())
                .name(obj.name())
                .description(obj.description())
                .type(obj.type())
                .price(obj.price())
                .createdAt(obj.createdAt())
                .imagePath(obj.imagePath())
                .build();
    }
}
