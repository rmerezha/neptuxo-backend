package space.neptuxo.dao;

import space.neptuxo.entity.User;

import java.sql.Connection;
import java.util.Optional;


public interface AbstractUserDao extends Dao<User, Long> {

    Optional<User> findByEmail(String email, Connection connection);

    Optional<User> findByUsername(String username, Connection connection);
}
