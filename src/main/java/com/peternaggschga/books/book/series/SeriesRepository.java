package com.peternaggschga.books.book.series;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

/**
 * An interface defining custom queries. Extends {@link CrudRepository} of {@link Series}.
 */
@Repository
public interface SeriesRepository extends CrudRepository<Series, Long> {

    /**
     * Returns all saved {@link Series} instances.
     *
     * @return a {@link Streamable} containing all {@link Series} instances.
     */
    @Override
    Streamable<Series> findAll();
}
