package space.neptuxo.dao;

public interface Dao<T, K> {

    T find(K id);

    boolean remove(K id);

    void save(T obj);

    void update(T obj);
    
}
