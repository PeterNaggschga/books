package com.peternaggschga.books.books.series;

import com.peternaggschga.books.books.book.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;

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

    /**
     * Returns all {@link Series} that contain the given {@link Book}.
     *
     * @param book must not be null.
     * @return a {@link Streamable} containing {@link Series}.
     */
    Streamable<Series> findByBooksContains(@NotNull Book book);
}
