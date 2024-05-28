package space.neptuxo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rmerezha.di.model.BeanWithMocks;
import space.neptuxo.dto.OrderDto;
import space.neptuxo.entity.OrderStatus;
import space.neptuxo.exception.SQLConstraintException;
import space.neptuxo.repository.AbstractOrderRepository;
import space.neptuxo.util.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    private OrderService service;
    private AbstractOrderRepository repository;
    private JsonBuilderFactory jsonBuilderFactory;
    private JsonParser jsonParser;
    private JsonBuilder jsonBuilder;

    @BeforeEach
    void setUp() {
        BeanWithMocks<? extends OrderService> beanForTest = DependencyInjector.getBeanForTest(OrderService.class);
        service = beanForTest.bean();
        repository = (AbstractOrderRepository) beanForTest.mocks().get(AbstractOrderRepository.class);
        jsonBuilderFactory = (JsonBuilderFactory) beanForTest.mocks().get(JsonBuilderFactory.class);
        jsonBuilder = mock(JsonBuilder.class);
        jsonParser = (JsonParser) beanForTest.mocks().get(JsonParser.class);
    }

    @Test
    void findByUserId() {
        long userId = 100;
        List<OrderDto> orders = List.of(OrderDto.builder()
                .status(OrderStatus.NEW)
                .address("ASd")
                .build());
        String expected = "{\"status\":\"success\",\"data\":{\"orders\":[{\"id\":null,\"productId\":0,\"customerId\":0,\"address\":\"ASd\",\"status\":\"NEW\",\"createdAt\":null}]}}";
        when(repository.findByUserId(userId)).thenReturn(orders);
        when(jsonBuilderFactory.create()).thenReturn(jsonBuilder);
        when(jsonBuilder.setStatus(any())).thenReturn(jsonBuilder);
        when(jsonBuilder.setData("orders", orders)).thenReturn(jsonBuilder);
        when(jsonBuilder.build()).thenReturn(expected);

        assertEquals(expected, service.findByUserId(userId));
    }

    @Test
    void createSuccess() {
        when(jsonBuilderFactory.create()).thenReturn(jsonBuilder);
        String json = """
        {
          "productId": 12345,
          "customerId": 67890,
          "address": "123 Main St, Anytown, USA",
        }
        """;
        OrderDto order = OrderDto.builder()
                .productId(12345L)
                .customerId(67890L)
                .address("123 Main St, Anytown, USA")
                .build();
        String expected = "\"status\": \"SUCCESS\"";
        InputStream reqJson = new ByteArrayInputStream(json.getBytes());
        when(jsonParser.parse(reqJson, OrderDto.class)).thenReturn(order);
        when(jsonBuilder.setStatus(any())).thenReturn(jsonBuilder);
        when(jsonBuilder.build()).thenReturn(expected);

        assertEquals(expected, service.create(reqJson));
    }

    @Test
    void createFail() {
        when(jsonBuilderFactory.create()).thenReturn(jsonBuilder);
        String json = """
        {
          "customerId": 67890,
          "address": "123 Main St, Anytown, USA",
        }
        """;
        OrderDto order = OrderDto.builder()
                .customerId(67890L)
                .address("123 Main St, Anytown, USA")
                .build();
        String expected = "\"status\": \"FAIL\"";
        when(jsonParser.parse(any(InputStream.class), eq(OrderDto.class))).thenReturn(order);
        doThrow(SQLConstraintException.class).when(repository).create(order);
        when(jsonBuilder.setStatus(Status.FAIL)).thenReturn(jsonBuilder);
        when(jsonBuilder.build()).thenReturn(expected);

        assertEquals(expected, service.create(new ByteArrayInputStream(json.getBytes())));
    }
}