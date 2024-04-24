package space.neptuxo.dao;

import java.sql.Connection;

public class UserDao extends UserBaseDao{

    public UserDao(Connection connection) {
        super(connection);
    }

}
