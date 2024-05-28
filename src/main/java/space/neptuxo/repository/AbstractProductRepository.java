package space.neptuxo.repository;

import space.neptuxo.dto.ProductDto;
import space.neptuxo.dto.ProductFilterDto;

import java.util.List;

public interface AbstractProductRepository {

    void create(ProductDto product);

    boolean delete(long userId, long productId);

    List<ProductDto> findByFilter(ProductFilterDto filterDto);
}
