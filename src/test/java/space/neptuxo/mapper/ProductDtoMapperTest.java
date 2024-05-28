package space.neptuxo.mapper;

import org.junit.jupiter.api.Test;
import space.neptuxo.dto.ProductDto;
import space.neptuxo.entity.Product;
import space.neptuxo.entity.ProductType;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductDtoMapperTest {

    private final ProductDtoMapper mapper = new ProductDtoMapper();

    @Test
    void map() {

        ProductDto dto = ProductDto.builder()
                .id(56)
                .createdBy(5)
                .name("name")
                .description("sss")
                .type(ProductType.ELECTRONICS)
                .price(5)
                .createdAt(LocalDateTime.of(2022, 5, 5, 5, 5))
                .imagePath("/images")
                .build();

        Product actual = mapper.map(dto);

        Product expected = Product.builder()
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