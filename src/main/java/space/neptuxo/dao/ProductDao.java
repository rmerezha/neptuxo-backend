package space.neptuxo.dao;

import java.sql.Connection;

public class ProductDao extends ProductBaseDao{

    public ProductDao(Connection connection) {
        super(connection);
    }

}
