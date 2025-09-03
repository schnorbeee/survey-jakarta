package com.dynata.surveyhw.repositories.impl;

import com.dynata.surveyhw.repositories.GenericRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public abstract class GenericRepositoryImpl<T, ID> implements GenericRepository<T, ID> {

    @PersistenceContext(unitName = "surveyPU")
    protected EntityManager em;

    private final Class<T> entityClass;

    protected GenericRepositoryImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public T save(T entity) {
        return em.merge(entity);
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(em.find(entityClass, id));
    }

    @Override
    public List<T> findAll() {
        return em.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass)
                .getResultList();
    }

    @Override
    public void delete(T entity) {
        em.remove(em.contains(entity) ? entity : em.merge(entity));
    }

    @Override
    public void deleteById(ID id) {
        findById(id).ifPresent(this::delete);
    }

    @Override
    public void saveAll(List<T> entities) {
        entities.forEach(this::save);
    }

    @Override
    public void deleteAll() {
        em.createQuery("DELETE FROM " + entityClass.getSimpleName()).executeUpdate();
    }
}
