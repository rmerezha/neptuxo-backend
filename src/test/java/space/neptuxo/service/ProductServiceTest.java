package space.neptuxo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rmerezha.di.model.BeanWithMocks;
import space.neptuxo.dto.ProductDto;
import space.neptuxo.dto.ProductFilterDto;
import space.neptuxo.entity.ProductType;
import space.neptuxo.exception.ActiveOrderExistsException;
import space.neptuxo.exception.SQLConstraintException;
import space.neptuxo.repository.AbstractProductRepository;
import space.neptuxo.util.DependencyInjector;
import space.neptuxo.util.JsonBuilder;
import space.neptuxo.util.JsonBuilderFactory;
import space.neptuxo.util.JsonParser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    private ProductService service;
    private AbstractProductRepository repository;
    private JsonParser jsonParser;
    private JsonBuilder jsonBuilder;

    @BeforeEach
    void setUp() {
        BeanWithMocks<ProductService> beanForTest = DependencyInjector.getBeanForTest(ProductService.class);
        service = beanForTest.bean();
        repository = (AbstractProductRepository) beanForTest.mocks().get(AbstractProductRepository.class);
        JsonBuilderFactory jsonBuilderFactory = (JsonBuilderFactory) beanForTest.mocks().get(JsonBuilderFactory.class);
        jsonBuilder = mock(JsonBuilder.class);
        jsonParser = (JsonParser) beanForTest.mocks().get(JsonParser.class);
        doReturn(jsonBuilder).when(jsonBuilderFactory).create();
    }

    @Test
    void createSuccess() {
        String json = """
        {
          "createdBy": 100,
          "name": "Product Name",
          "description": "Product Description",
          "type": "ELECTRONICS",
          "price": 10,
          "imagePath": "/images/product.jpg"
        }
        """;
        ProductDto product = ProductDto.builder()
                .createdBy(100L)
                .name("Product Name")
                .description("Product Description")
                .type(ProductType.ELECTRONICS)
                .price(10)
                .imagePath("/images/product.jpg")
                .build();
        String expected = "{\"status\":\"success\"}";

        when(jsonParser.parse(any(InputStream.class), eq(ProductDto.class))).thenReturn(product);
        when(jsonBuilder.setStatus(any())).thenReturn(jsonBuilder);
        when(jsonBuilder.build()).thenReturn(expected);

        assertEquals(expected, service.create(new ByteArrayInputStream(json.getBytes())));
    }

    @Test
    void createFail() {
        String json = """
        {
          "createdBy": 100,
          "name": "Product Name",
          "description": "Product Description",
          "type": "ELECTRONICS",
          "price": 10,
          "imagePath": "/images/product.jpg"
        }
        """;
        ProductDto product = ProductDto.builder()
                .createdBy(100L)
                .name("Product Name")
                .description("Product Description")
                .type(ProductType.ELECTRONICS)
                .price(10)
                .imagePath("/images/product.jpg")
                .build();
        String expected = "{\"status\":\"fail\"}";

        when(jsonParser.parse(any(InputStream.class), eq(ProductDto.class))).thenReturn(product);
        doThrow(SQLConstraintException.class).when(repository).create(product);
        when(jsonBuilder.setStatus(any())).thenReturn(jsonBuilder);
        when(jsonBuilder.build()).thenReturn(expected);

        assertEquals(expected, service.create(new ByteArrayInputStream(json.getBytes())));
    }

    @Test
    void deleteSuccess() {
        long userId = 100;
        long productId = 200;
        String expected = "{\"status\":\"success\"}";

        when(repository.delete(userId, productId)).thenReturn(true);
        when(jsonBuilder.setStatus(any())).thenReturn(jsonBuilder);
        when(jsonBuilder.build()).thenReturn(expected);

        assertEquals(expected, service.delete(userId, productId));
    }

    @Test
    void deleteFail_ProductNotExist() {
        long userId = 100;
        long productId = 200;
        String expected = "{\"status\":\"fail\",\"errors\":[\"PRODUCT_NOT_EXIST\"]}";

        when(repository.delete(userId, productId)).thenReturn(false);
        when(jsonBuilder.setStatus(any())).thenReturn(jsonBuilder);
        when(jsonBuilder.setErrors(any())).thenReturn(jsonBuilder);
        when(jsonBuilder.build()).thenReturn(expected);

        assertEquals(expected, service.delete(userId, productId));
    }

    @Test
    void deleteFail_ActiveOrderExists() {
        long userId = 100;
        long productId = 200;
        String expected = "{\"status\":\"fail\",\"errors\":[\"HAS_ACTIVE_ORDERS\"]}";

        doThrow(ActiveOrderExistsException.class).when(repository).delete(userId, productId);
        when(jsonBuilder.setStatus(any())).thenReturn(jsonBuilder);
        when(jsonBuilder.setErrors(any())).thenReturn(jsonBuilder);
        when(jsonBuilder.build()).thenReturn(expected);

        assertEquals(expected, service.delete(userId, productId));
    }

    @Test
    void search() {
        String json = """
        {
          "type": "ELECTRONICS",
          "minCount": 1
        }
        """;
        ProductFilterDto filter = ProductFilterDto.builder()
                .types(List.of(ProductType.ELECTRONICS))
                .limit(20)
                .build();
        List<ProductDto> products = List.of(
                ProductDto.builder()
                        .id(1L)
                        .createdBy(100L)
                        .name("Product Name")
                        .description("Product Description")
                        .type(ProductType.ELECTRONICS)
                        .price(10)
                        .createdAt(LocalDateTime.now())
                        .imagePath("/images/product.jpg")
                        .build()
        );
        String expected = "{\"status\":\"success\",\"data\":{\"products\":[{\"id\":1,\"createdBy\":100,\"name\":\"Product Name\",\"description\":\"Product Description\",\"type\":\"ELECTRONICS\",\"price\":10,\"createdAt\":\"2024-05-28T12:34:56\",\"imagePath\":\"/images/product.jpg\"}]}}";

        when(jsonParser.parse(any(InputStream.class), eq(ProductFilterDto.class))).thenReturn(filter);
        when(repository.findByFilter(filter)).thenReturn(products);
        when(jsonBuilder.setStatus(any())).thenReturn(jsonBuilder);
        when(jsonBuilder.setData(anyString(), any(List.class))).thenReturn(jsonBuilder);
        when(jsonBuilder.build()).thenReturn(expected);

        assertEquals(expected, service.search(new ByteArrayInputStream(json.getBytes())));
    }

}