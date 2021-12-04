package com.peternaggschga.books.author;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * An interface defining custom queries. Extends {@link CrudRepository} of {@link Author}s.
 */
@Repository
public interface AuthorRepository extends CrudRepository<Author, Long> {
}
