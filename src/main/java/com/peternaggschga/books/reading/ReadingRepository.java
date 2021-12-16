package com.peternaggschga.books.reading;

import org.springframework.data.domain.Sort;
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
     * Wrapper function of {@link ReadingRepository#findByOrderByBeginningDesc(Sort)}.
     *
     * @return a {@link Streamable} containing all {@link Reading} instances.
     * @see ReadingRepository#findByOrderByBeginningDesc(Sort)
     */
    @Override
    default Streamable<Reading> findAll() {
        return findByOrderByBeginningDesc(Sort.by(Sort.Direction.DESC));
    }

    /**
     * Finds all saved {@link Reading} instances, ordered by beginning.
     *
     * @param sort must be valid {@link Sort} instance.
     * @return a {@link Streamable} containing all {@link Reading} instances.
     * @see ReadingRepository#findAll()
     */
    Streamable<Reading> findByOrderByBeginningDesc(Sort sort);
}
