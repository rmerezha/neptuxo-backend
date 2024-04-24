package space.neptuxo.mapper;

import org.junit.jupiter.api.Test;
import space.neptuxo.dto.ProductDto;
import space.neptuxo.entity.Product;
import space.neptuxo.entity.ProductType;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {

    private final ProductMapper mapper = new ProductMapper();

    @Test
    void map() {

        ProductDto dto = ProductDto.builder()
                .id(56)
                .createdBy(5)
                .description("sss")
                .type(ProductType.ELECTRONICS)
                .count(5)
                .createdAt(LocalDateTime.of(2022, 5, 5, 5, 5))
                .imagePath("/images")
                .build();

        Product actual = mapper.map(dto);

        Product expected = Product.builder()
                .id(56)
                .createdBy(5)
                .description("sss")
                .type(ProductType.ELECTRONICS)
                .count(5)
                .createdAt(LocalDateTime.of(2022, 5, 5, 5, 5))
                .imagePath("/images")
                .build();
        assertEquals(expected, actual);

    }
}