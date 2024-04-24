package space.neptuxo.dao;

import java.sql.Connection;

public class OrderDao extends OrderBaseDao {

    public OrderDao(Connection connection) {
        super(connection);
    }

}
