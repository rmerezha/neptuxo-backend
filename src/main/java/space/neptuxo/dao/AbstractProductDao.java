package space.neptuxo.dao;

import space.neptuxo.dto.ProductFilterDto;
import space.neptuxo.entity.Product;

import java.sql.Connection;
import java.util.List;


public interface AbstractProductDao extends Dao<Product, Long> {

    List<Product> findByUserId(long id, Connection connection);

    List<Product> findByFilter(ProductFilterDto filterDto, Connection connection);
}
