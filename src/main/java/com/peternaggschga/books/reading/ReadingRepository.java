package com.peternaggschga.books.reading;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

/**
 * An interface defining custom queries. Extends {@link CrudRepository} of {@link Reading}s.
 */
@Repository
public interface ReadingRepository extends CrudRepository<Reading, Long> {

    /**
     * Returns all saved {@link Reading} instances, ordered by beginning descending.
     *
     * @return a {@link Streamable} containing all {@link Reading} instances.
     */
    @Query("SELECT r FROM Reading r ORDER BY r.beginning DESC")
    @Override
    Streamable<Reading> findAll();
}
