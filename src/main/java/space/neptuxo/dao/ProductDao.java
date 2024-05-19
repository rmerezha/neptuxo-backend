package space.neptuxo.dao;

import java.sql.Connection;

public class ProductDao extends AbstractProductDao {

    public ProductDao(Connection connection) {
        super(connection);
    }

}
