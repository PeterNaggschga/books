package com.peternaggschga.books.book;

import com.peternaggschga.books.author.Author;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

/**
 * Service managing access to the {@link BookRepository} and {@link Book} instances.
 */
@Service
@Transactional
public class BookManagement {
    @NotNull
    private final BookRepository repository;

    /**
     * Creates a new {@link BookManagement} instance with the given {@link BookRepository}.
     *
     * @param repository must not be null.
     */
    public BookManagement(@NotNull BookRepository repository) {
        this.repository = repository;
    }

    /**
     * Creates a new {@link Book} instance with the given title, authors, published, isbn, pages and language.
     * The new instance is saved into the repository.
     *
     * @param title     must not be null or blank.
     * @param authors   must not be null or empty.
     * @param published must not be null.
     * @param isbn      must not be null, must match {@link Book#ISBN_REGEX}.
     * @param pages     must be positive.
     * @param language  must not be null.
     */
    public Book createBook(@NotNull @NotBlank String title, @NotNull @NotEmpty List<Author> authors,
                           @NotNull LocalDate published, @NotNull String isbn, @Positive int pages,
                           @NotNull Locale language) {
        return repository.save(new Book(title, authors, published, isbn, pages, language));
    }

    /**
     * Returns all {@link Book}s present in repository.
     *
     * @return an {@link Iterable} containing all {@link Book} instances in repository.
     */
    public Iterable<Book> findAll() {
        return repository.findAll();
    }
}
