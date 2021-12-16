package com.peternaggschga.books.book;

import com.peternaggschga.books.author.Author;
import com.peternaggschga.books.author.AuthorManagement;
import com.peternaggschga.books.book.series.CreateSeriesForm;
import com.peternaggschga.books.book.series.Series;
import com.peternaggschga.books.book.series.SeriesRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service managing access to the {@link BookRepository} and {@link Book} instances as well as the
 * {@link SeriesRepository} and {@link Series} instances.
 */
@Service
@Transactional
public class BookManagement {
    public static final Locale[] LANGUAGES = {Locale.GERMAN, Locale.ENGLISH};
    @NotNull
    private final BookRepository bookRepository;
    @NotNull
    private final SeriesRepository seriesRepository;
    @NotNull
    private final AuthorManagement authorManagement;

    /**
     * Creates a new {@link BookManagement} instance with the given {@link BookRepository} and {@link SeriesRepository}.
     *
     * @param bookRepository   must not be null.
     * @param seriesRepository must not be null.
     * @param authorManagement must not be null.
     */
    public BookManagement(@NotNull BookRepository bookRepository, @NotNull SeriesRepository seriesRepository,
                          @NotNull AuthorManagement authorManagement) {
        this.bookRepository = bookRepository;
        this.seriesRepository = seriesRepository;
        this.authorManagement = authorManagement;
    }

    /**
     * Creates a new {@link Book} instance with the given title, authors, published, isbn, pages and language.
     * The new instance is saved into the {@link BookRepository}.
     *
     * @param title     must not be null or blank.
     * @param authors   must not be null or empty.
     * @param published must not be null.
     * @param isbn      must not be null, must match {@link Book#ISBN_REGEX}.
     * @param pages     must be positive.
     * @param language  must not be null.
     * @return the new {@link Book} instance.
     */
    public Book createBook(@NotNull @NotBlank String title, @NotNull @NotEmpty Collection<Author> authors,
                           @NotNull LocalDate published, @NotNull String isbn, @Positive int pages,
                           @NotNull Locale language) {
        return bookRepository.save(new Book(title, authors, published, isbn, pages, language));
    }

    /**
     * Creates a new {@link Book} instance with the given {@link CreateBookForm} and adds it to the given series.
     * The new instance is saved into the {@link BookRepository}.
     * Wrapper function of {@link BookManagement#createBook(String, Collection, LocalDate, String, int, Locale)}.
     *
     * @param form must not be null or invalid.
     * @return the new {@link Book} instance.
     * @see BookManagement#createBook(String, Collection, LocalDate, String, int, Locale)
     * @see BookManagement#addBooksToSeries(Book, long)
     */
    public Book createBook(@NotNull @Valid CreateBookForm form) {
        return createBook(form.getTitle(), form.getAuthors().stream().map(authorManagement::findAuthorById)
                .collect(Collectors.toSet()), form.getPublished(), form.getIsbn(), form.getPages(), form.getLanguage());
    }

    /**
     * Returns all {@link Book}s present in {@link BookRepository}.
     *
     * @return an {@link Iterable} containing all {@link Book} instances in {@link BookRepository}.
     */
    public Streamable<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * Returns the {@link Book} referenced by the given id.
     * If the id does not exist, a {@link java.util.NoSuchElementException} is thrown.
     *
     * @param id must be valid, else {@link java.util.NoSuchElementException} is thrown.
     * @return the {@link Book} referenced by id.
     */
    public Book findBookById(long id) {
        return bookRepository.findById(id).orElseThrow();
    }

    /**
     * Creates a new {@link Series} instance with the given title and books.
     * The new instance is saved into the {@link SeriesRepository}.
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
     * The new instance is saved into the {@link SeriesRepository}.
     * Wrapper function of {@link BookManagement#createSeries(String, Collection)}.
     *
     * @param form must not be null or invalid.
     * @return the new {@link Series} instance.
     * @see BookManagement#createSeries(String, Collection)
     */
    public Series createSeries(@NotNull @Valid CreateSeriesForm form) {
        return form.getBooks() == null ? createSeries(form.getTitle(), null) :
                createSeries(form.getTitle(), form.getBooks().stream().map(this::findBookById)
                        .collect(Collectors.toSet()));
    }

    /**
     * Adds all {@link Book}s of the given {@link Collection} to the {@link Series} referenced by the given id.
     *
     * @param books    can be null.
     * @param seriesId must be valid.
     * @return the updated {@link Series}.
     * @see BookManagement#addBooksToSeries(Book, long)
     * @see BookManagement#findSeriesById(long)
     * @see Series#addAll(Collection)
     */
    public Series addBooksToSeries(Collection<Book> books, long seriesId) {
        Series series = findSeriesById(seriesId);
        series.addAll(books);
        return seriesRepository.save(series);
    }

    /**
     * Adds all {@link Book}s of the given {@link Collection} to the {@link Series} referenced by the given id.
     * Wrapper function of {@link BookManagement#addBooksToSeries(Collection, long)}.
     *
     * @param book     must not be null.
     * @param seriesId must be valid.
     * @return the updated {@link Series}.
     * @see BookManagement#addBooksToSeries(Collection, long)
     */
    public Series addBooksToSeries(@NotNull Book book, long seriesId) {
        return addBooksToSeries(Set.of(book), seriesId);
    }

    /**
     * Returns all {@link Series} present in {@link SeriesRepository}.
     *
     * @return an {@link Iterable} containing all {@link Series} instances in {@link SeriesRepository}.
     */
    public Streamable<Series> findAllSeries() {
        return seriesRepository.findAll();
    }

    /**
     * Returns the {@link Series} referenced by the given id.
     * If the id does not exist, a {@link java.util.NoSuchElementException} is thrown.
     *
     * @param id must be valid, else {@link java.util.NoSuchElementException} is thrown.
     * @return the {@link Series} referenced by id.
     */
    public Series findSeriesById(long id) {
        return seriesRepository.findById(id).orElseThrow();
    }
}
