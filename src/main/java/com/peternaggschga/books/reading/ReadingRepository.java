package com.peternaggschga.books.reading;

import com.peternaggschga.books.books.book.Book;
import com.peternaggschga.books.books.series.Series;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;

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

    /**
     * Returns all {@link Series} that contain the given {@link Book}.
     *
     * @param book must not be null.
     * @return a {@link Streamable} containing {@link Series}.
     */
    Streamable<Reading> findReadingsByBook(@NotNull Book book);
}
