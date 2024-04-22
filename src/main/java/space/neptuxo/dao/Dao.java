package space.neptuxo.dao;

import java.util.Optional;

public interface Dao<T, K> {

    Optional<T> findById(K id);

    boolean remove(K id);

    boolean update(T obj);

    void save(T obj);

}
