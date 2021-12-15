package com.peternaggschga.books.book;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * An interface defining custom queries. Extends {@link CrudRepository} of {@link Book}s.
 */
@Repository
public interface BookRepository extends CrudRepository<Book, Long> {
}
