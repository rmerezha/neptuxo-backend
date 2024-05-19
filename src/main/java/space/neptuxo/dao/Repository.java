package space.neptuxo.dao;

import java.util.Optional;

public interface Repository<T> {

    Optional<T> findById(T obj);

    boolean remove(T obj);

    boolean update(T obj);

    void save(T obj);

}
