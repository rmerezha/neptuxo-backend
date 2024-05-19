package space.neptuxo.dao;

import java.sql.Connection;

public class OrderDao extends AbstractOrderDao {

    public OrderDao(Connection connection) {
        super(connection);
    }

}
