package space.neptuxo.dao;

import java.sql.Connection;
import java.util.Optional;

public interface Dao<T, K> {

    Optional<T> findById(K id, Connection connection);

    boolean remove(K id, Connection connection);

    boolean update(T obj, Connection connection);

    void save(T obj, Connection connection);

}
