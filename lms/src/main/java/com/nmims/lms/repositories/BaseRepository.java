package com.nmims.lms.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean // Ensures this interface is not instantiated as a Spring bean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID> {

    Optional<T> findById(ID id);

    boolean existsById(ID id);

    void deleteById(ID id);

    List<T> findAll();

    <S extends T> S save(S entity);

}
