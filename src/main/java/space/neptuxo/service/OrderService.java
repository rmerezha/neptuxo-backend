package space.neptuxo.service;

import org.rmerezha.di.annotation.Inject;
import space.neptuxo.dto.OrderDto;
import space.neptuxo.exception.SQLConstraintException;
import space.neptuxo.repository.AbstractOrderRepository;
import space.neptuxo.util.JsonBuilder;
import space.neptuxo.util.JsonBuilderFactory;
import space.neptuxo.util.JsonParser;
import space.neptuxo.util.Status;

import java.io.InputStream;
import java.util.List;

public class OrderService {
    @Inject
    private AbstractOrderRepository repository;
    @Inject
    private JsonBuilderFactory jsonBuilderFactory;
    @Inject
    private JsonParser jsonParser;

    private static final String KEY_ORDERS = "orders";

    public String findByUserId(long userId) {
        List<OrderDto> orders = repository.findByUserId(userId);

        return jsonBuilderFactory.create()
                .setStatus(Status.SUCCESS)
                .setData(KEY_ORDERS, orders)
                .build();
    }

    public String create(InputStream reqJson) {
        JsonBuilder jsonBuilder = jsonBuilderFactory.create();
        try {
            OrderDto orderDto = jsonParser.parse(reqJson, OrderDto.class);

            repository.create(orderDto);

            return jsonBuilder.setStatus(Status.SUCCESS)
                    .build();
        } catch (SQLConstraintException e) {
            return jsonBuilder.setStatus(Status.FAIL).build();
        }
    }
}
