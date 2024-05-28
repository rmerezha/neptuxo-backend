package space.neptuxo.mapper;

import org.junit.jupiter.api.Test;
import space.neptuxo.dto.ProductDto;
import space.neptuxo.entity.Product;
import space.neptuxo.entity.ProductType;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductMapperTest {

    private final ProductMapper mapper = new ProductMapper();

    @Test
    void map() {
        Product entity = Product.builder()
                .id(56)
                .createdBy(5)
                .name("name")
                .description("sss")
                .type(ProductType.ELECTRONICS)
                .price(5)
                .createdAt(LocalDateTime.of(2022, 5, 5, 5, 5))
                .imagePath("/images")
                .build();

        ProductDto actual = mapper.map(entity);

        ProductDto expected = ProductDto.builder()
                .id(56)
                .createdBy(5)
                .name("name")
                .description("sss")
                .type(ProductType.ELECTRONICS)
                .price(5)
                .createdAt(LocalDateTime.of(2022, 5, 5, 5, 5))
                .imagePath("/images")
                .build();
        assertEquals(expected, actual);
    }

}