package com.peternaggschga.books.book;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * An interface defining custom queries. Extends {@link CrudRepository} of {@link Series}.
 */
@Repository
public interface SeriesRepository extends CrudRepository<Series, Long> {
}
