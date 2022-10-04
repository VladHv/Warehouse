package ua.foxminded.herasimov.warehouse.service;

import java.util.List;

public interface Service<K extends Number, T> {

    T create(T entity);

    T findById(K id);

    T update(T entity, K id);

    void delete(K id);

    void delete(T entity);

    List<T> findAll();
}
