package com.peternaggschga.books.books.book;

import com.peternaggschga.books.author.Author;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;

/**
 * An interface defining custom queries. Extends {@link CrudRepository} of {@link Book}s.
 */
@Repository
public interface BookRepository extends CrudRepository<Book, Long> {

    /**
     * Returns all saved {@link Book} instances.
     *
     * @return a {@link Streamable} containing all {@link Book} instances.
     */
    @Override
    Streamable<Book> findAll();

    /**
     * Returns all {@link Book}s associated with the given {@link Author}.
     *
     * @param author must not be null.
     * @return a {@link Streamable} containing {@link Book}s.
     */
    Streamable<Book> findByAuthorsContains(@NotNull Author author);
}
