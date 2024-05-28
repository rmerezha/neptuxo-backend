package space.neptuxo.service;

import org.rmerezha.di.annotation.Inject;
import space.neptuxo.dto.ProductDto;
import space.neptuxo.dto.ProductFilterDto;
import space.neptuxo.exception.ActiveOrderExistsException;
import space.neptuxo.exception.SQLConstraintException;
import space.neptuxo.repository.AbstractProductRepository;
import space.neptuxo.util.Error;
import space.neptuxo.util.*;

import java.io.InputStream;
import java.util.List;

public class ProductService {

    @Inject
    private AbstractProductRepository repository;

    @Inject
    private JsonBuilderFactory jsonBuilderFactory;

    @Inject
    private JsonParser jsonParser;

    private static final String KEY_PRODUCTS = "products";


    public String create(InputStream reqJson) {
        JsonBuilder jsonBuilder = jsonBuilderFactory.create();
        try {
            ProductDto product = jsonParser.parse(reqJson, ProductDto.class);

            repository.create(product);
            return jsonBuilder.setStatus(Status.SUCCESS).build();

        } catch (SQLConstraintException e) {
            return jsonBuilder.setStatus(Status.FAIL).build();
        }
    }

    public String delete(long userId, long productId) {
        JsonBuilder jsonBuilder = jsonBuilderFactory.create();
        try {
            if (repository.delete(userId, productId)) {
                return jsonBuilder.setStatus(Status.SUCCESS).build();
            }
            return jsonBuilder.setStatus(Status.FAIL)
                    .setErrors(List.of(Error.PRODUCT_NOT_EXIST))
                    .build();
        } catch (ActiveOrderExistsException e) {
            return jsonBuilder.setStatus(Status.FAIL)
                    .setErrors(List.of(Error.HAS_ACTIVE_ORDERS))
                    .build();
        }
    }

    public String search(InputStream reqJson) {
        ProductFilterDto filterDto = jsonParser.parse(reqJson, ProductFilterDto.class);
        JsonBuilder jsonBuilder = jsonBuilderFactory.create();

        List<ProductDto> products = repository.findByFilter(filterDto);

        return jsonBuilder.setStatus(Status.SUCCESS)
                .setData(KEY_PRODUCTS, products)
                .build();
    }
}
