package com.dynata.surveyhw.repositories;

import java.util.List;
import java.util.Optional;

public interface GenericRepository<T, ID> {

    T save(T entity);

    Optional<T> findById(ID id);

    List<T> findAll();

    void delete(T entity);

    void deleteById(ID id);

    void saveAll(List<T> entities);

    void deleteAll();
}
