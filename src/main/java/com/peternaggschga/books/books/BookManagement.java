package com.peternaggschga.books.books;

import com.peternaggschga.books.author.Author;
import com.peternaggschga.books.books.book.Book;
import com.peternaggschga.books.books.book.BookRepository;
import com.peternaggschga.books.books.book.CreateBookForm;
import com.peternaggschga.books.books.series.CreateSeriesForm;
import com.peternaggschga.books.books.series.Series;
import com.peternaggschga.books.books.series.SeriesRepository;
import com.peternaggschga.books.reading.ReadingManagement;
import lombok.NonNull;
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
    private final ReadingManagement readingManagement;

    /**
     * Creates a new {@link BookManagement} instance with the given {@link BookRepository}, {@link SeriesRepository} and
     * {@link ReadingManagement}.
     *
     * @param bookRepository    must not be null.
     * @param seriesRepository  must not be null.
     * @param readingManagement must not be null.
     */
    public BookManagement(@NonNull BookRepository bookRepository, @NonNull SeriesRepository seriesRepository,
                          @NonNull ReadingManagement readingManagement) {
        this.bookRepository = bookRepository;
        this.seriesRepository = seriesRepository;
        this.readingManagement = readingManagement;
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
    public Book createBook(@NonNull @NotBlank String title, @NonNull @NotEmpty Collection<Author> authors,
                           @NonNull LocalDate published, @NonNull String isbn, @Positive int pages,
                           @NonNull Locale language) {
        return bookRepository.save(new Book(title, authors, published, isbn, pages, language));
    }

    /**
     * Creates a new {@link Book} instance with the given {@link CreateBookForm}. The new instance is saved into the
     * {@link BookRepository}.
     * Wrapper function of {@link BookManagement#createBook(String, Collection, LocalDate, String, int, Locale)}.
     *
     * @param form must not be null or invalid.
     * @return the new {@link Book} instance.
     * @see BookManagement#createBook(String, Collection, LocalDate, String, int, Locale)
     * @see BookManagement#addBooksToSeries(Book, long)
     */
    public Book createBook(@NonNull @Valid CreateBookForm form, Collection<Author> authors) {
        return createBook(form.getTitle(), authors, form.getPublished(), form.getIsbn(), form.getPages(),
                form.getLanguage());
    }

    /**
     * Deletes the given {@link Book} from {@link BookRepository}.
     *
     * @param book must not be null.
     */
    public void deleteBook(@NonNull Book book) {
        for (Series series : findSeriesByBook(book)) {
            series.remove(book);
            seriesRepository.save(series);
        }
        readingManagement.deleteReadingsByBook(book);
        bookRepository.delete(book);
    }

    /**
     * Deletes the {@link Book} referenced by the given id from {@link BookRepository}.
     * Wrapper function of {@link BookManagement#deleteBook(Book)}.
     *
     * @param id must be valid.
     * @see BookManagement#deleteBook(Book)
     */
    public void deleteBook(long id) {
        deleteBook(findBookById(id));
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
     * Returns the number of {@link Book}s saved in {@link BookRepository}.
     *
     * @return a long counting the number of {@link Book}s.
     */
    public long getBookCount() {
        return bookRepository.count();
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
     * Returns all {@link Book}s associated with the given {@link Author}.
     *
     * @param author must not be null.
     * @return a {@link Streamable} containing {@link Book}s.
     */
    public Streamable<Book> findBooksByAuthor(@NonNull Author author) {
        return bookRepository.findByAuthorsContains(author);
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
    public Series createSeries(@NonNull @NotBlank String title, Collection<Book> books) {
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
    public Series createSeries(@NonNull @Valid CreateSeriesForm form) {
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
    public Series addBooksToSeries(@NonNull Book book, long seriesId) {
        return addBooksToSeries(Set.of(book), seriesId);
    }

    /**
     * Deletes the {@link Series} referenced by the given id from {@link SeriesRepository}.
     *
     * @param id must be valid.
     */
    public void deleteSeries(long id) {
        seriesRepository.deleteById(id);
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

    /**
     * Returns all {@link Series} saved in {@link SeriesRepository} that contain the given {@link Book}.
     *
     * @param book must not be null.
     * @return a {@link Streamable} containing {@link Series}.
     */
    public Streamable<Series> findSeriesByBook(@NonNull Book book) {
        return seriesRepository.findByBooksContains(book);
    }
}
