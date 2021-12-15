package com.peternaggschga.books.book;

import com.peternaggschga.books.author.Author;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Service managing access to the {@link BookRepository} and {@link Book} instances as well as the
 * {@link SeriesRepository} and {@link Series} instances.
 */
@Service
@Transactional
public class BookManagement {
    @NotNull
    private final BookRepository bookRepository;
    @NotNull
    private final SeriesRepository seriesRepository;

    /**
     * Creates a new {@link BookManagement} instance with the given {@link BookRepository} and {@link SeriesRepository}.
     *
     * @param bookRepository   must not be null.
     * @param seriesRepository must not be null.
     */
    public BookManagement(BookRepository bookRepository, SeriesRepository seriesRepository) {
        this.bookRepository = bookRepository;
        this.seriesRepository = seriesRepository;
    }

    /**
     * Creates a new {@link Book} instance with the given title, authors, published, isbn, pages and language.
     * The new instance is saved into the bookRepository.
     *
     * @param title     must not be null or blank.
     * @param authors   must not be null or empty.
     * @param published must not be null.
     * @param isbn      must not be null, must match {@link Book#ISBN_REGEX}.
     * @param pages     must be positive.
     * @param language  must not be null.
     * @return the new {@link Book} instance.
     */
    public Book createBook(@NotNull @NotBlank String title, @NotNull @NotEmpty List<Author> authors,
                           @NotNull LocalDate published, @NotNull String isbn, @Positive int pages,
                           @NotNull Locale language) {
        return bookRepository.save(new Book(title, authors, published, isbn, pages, language));
    }

    /**
     * Returns all {@link Book}s present in bookRepository.
     *
     * @return an {@link Iterable} containing all {@link Book} instances in bookRepository.
     */
    public Iterable<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * Returns the book with the given title.
     *
     * @param title must not be null or blank.
     * @return the {@link Book} with the given title.
     */
    public Book findBookByTitle(@NotNull @NotBlank String title) {
        return bookRepository.findByTitle(title);
    }

    /**
     * Creates a new {@link Series} instance with the given title and books.
     * The new instance is saved into the seriesRepository.
     *
     * @param title must not be null or blank.
     * @param books can be null.
     * @return the new {@link Series} instance.
     * @see BookManagement#createSeries(CreateSeriesForm)
     */
    public Series createSeries(@NotNull @NotBlank String title, Collection<Book> books) {
        return seriesRepository.save(new Series(title, books));
    }

    /**
     * Creates a new {@link Series} instance with the given {@link CreateSeriesForm}.
     * The new instance is saved into the seriesRepository.
     * Wrapper function of {@link BookManagement#createSeries(String, Collection)}.
     *
     * @param form must not be null or invalid.
     * @return the new {@link Series} instance.
     * @see BookManagement#createSeries(String, Collection)
     */
    public Series createSeries(@NotNull @Valid CreateSeriesForm form) {
        return createSeries(form.getTitle(), form.getBooks().stream().map(this::findBookByTitle)
                .collect(Collectors.toSet()));
    }

    /**
     * Returns all {@link Series} present in seriesRepository.
     *
     * @return an {@link Iterable} containing all {@link Series} instances in seriesRepository.
     */
    public Iterable<Series> findAllSeries() {
        return seriesRepository.findAll();
    }
}
