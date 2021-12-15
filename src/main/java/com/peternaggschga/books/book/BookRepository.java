package com.peternaggschga.books.book;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * An interface defining custom queries. Extends {@link CrudRepository} of {@link Book}s.
 */
@Repository
public interface BookRepository extends CrudRepository<Book, Long> {

    /**
     * Returns the book with the given title.
     *
     * @param title must not be null or blank.
     * @return the {@link Book} with the given title.
     */
    Book findByTitle(@NotNull @NotBlank String title);
}
