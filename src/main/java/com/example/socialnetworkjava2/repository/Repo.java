package com.example.socialnetworkjava2.repository;

import com.example.socialnetworkjava2.domain.Entity;
import com.example.socialnetworkjava2.exceptions.RepositoryException;
import com.example.socialnetworkjava2.utils.Event;
import com.example.socialnetworkjava2.utils.observer.Observer;

import java.util.List;

/**
 * CRUD operations repository interface
 * @param <ID> - type E must have an attribute of type ID
 * @param <E> -  type of entities saved in repository
 */

public interface Repo<ID, E extends Entity<Long>> {
    E findOne(Long id);
    List<E> findAll();
    E save(E entity);
    E delete(Long id);
    E update(E entity);
}