package com.peternaggschga.books.books;

import com.neovisionaries.i18n.CountryCode;
import com.peternaggschga.books.author.Author;
import com.peternaggschga.books.author.AuthorRepository;
import com.peternaggschga.books.books.book.Book;
import com.peternaggschga.books.books.book.BookRepository;
import com.peternaggschga.books.books.book.EditBookForm;
import com.peternaggschga.books.books.series.EditSeriesForm;
import com.peternaggschga.books.books.series.Series;
import com.peternaggschga.books.books.series.SeriesRepository;
import com.peternaggschga.books.reading.Reading;
import com.peternaggschga.books.reading.ReadingManagement;
import com.peternaggschga.books.reading.ReadingRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.util.Streamable;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BookManagementUnitTest {

    @BeforeAll
    static void canBeInstantiated() {
        try {
            new BookManagement(mock(BookRepository.class), mock(SeriesRepository.class), mock(ReadingManagement.class));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Nested
    class BookTests {
        @Nested
        @SpringBootTest
        class CreateBookTests {
            Set<Book> books = new HashSet<>();
            @Autowired
            AuthorRepository authorRepository;
            @Autowired
            BookRepository repository;
            BookManagement management;

            @BeforeEach
            void setup() {
                management = new BookManagement(repository, mock(SeriesRepository.class),
                        mock(ReadingManagement.class));
            }

            @AfterEach
            void tearDown() {
                repository.deleteAll(books);
                books.clear();
            }

            @Test
            void createBookAssertsTitleBlank() {
                EditBookForm form;
                try {
                    books.add(management.createBook("", List.of(mock(Author.class)), LocalDate.now(),
                            "978-3-492-70479-3", 1, Locale.GERMAN));
                    fail();
                } catch (IllegalArgumentException ignored) {
                    assertEquals(0, books.size());
                }
                form = new EditBookForm("", List.of(1L), LocalDate.now().toString(), "978-3-492-70479-3", 1,
                        Locale.GERMAN.toString(), Collections.emptyList());
                try {
                    books.add(management.createBook(form, List.of(mock(Author.class))));
                    fail();
                } catch (IllegalArgumentException ignored) {
                    assertEquals(0, books.size());
                }
                try {
                    books.add(management.createBook(" ", List.of(mock(Author.class)), LocalDate.now(),
                            "978-3-492-70479-3", 1, Locale.GERMAN));
                    fail();
                } catch (IllegalArgumentException ignored) {
                    assertEquals(0, books.size());
                }
                form = new EditBookForm(" ", List.of(1L), LocalDate.now().toString(), "978-3-492-70479-3", 1,
                        Locale.GERMAN.toString(), Collections.emptyList());
                try {
                    books.add(management.createBook(form, List.of(mock(Author.class))));
                    fail();
                } catch (IllegalArgumentException ignored) {
                    assertEquals(0, books.size());
                }
            }

            @Test
            void createBookAssertsAuthorsEmpty() {
                EditBookForm form;
                try {
                    books.add(management.createBook("Buch", Collections.emptyList(), LocalDate.now(),
                            "978-3-492-70479-3", 1, Locale.GERMAN));
                    fail();
                } catch (IllegalArgumentException ignored) {
                    assertEquals(0, books.size());
                }
                form = new EditBookForm("Buch", Collections.emptyList(), LocalDate.now().toString(),
                        "978-3-492-70479-3", 1, Locale.GERMAN.toString(), Collections.emptyList());
                try {
                    books.add(management.createBook(form, Collections.emptyList()));
                    fail();
                } catch (IllegalArgumentException ignored) {
                    assertEquals(0, books.size());
                }
            }

            @Test
            void createBookAssertsPagesZero() {
                EditBookForm form;
                try {
                    management.createBook("Buch", List.of(mock(Author.class)), LocalDate.now(), "978-3-492-70479-3", 0,
                            Locale.GERMAN);
                    fail();
                } catch (IllegalArgumentException ignored) {
                    assertEquals(0, books.size());
                }
                form = new EditBookForm("Buch", Collections.emptyList(), LocalDate.now().toString(),
                        "978-3-492-70479-3", 0, Locale.GERMAN.toString(), Collections.emptyList());
                try {
                    management.createBook(form, List.of(mock(Author.class)));
                    fail();
                } catch (IllegalArgumentException ignored) {
                    assertEquals(0, books.size());
                }
            }

            @Test
            void createBookAssertsPagesNegative() {
                EditBookForm form;
                try {
                    management.createBook("Buch", List.of(mock(Author.class)), LocalDate.now(), "978-3-492-70479-3", -1,
                            Locale.GERMAN);
                    fail();
                } catch (IllegalArgumentException ignored) {
                    assertEquals(0, books.size());
                }
                form = new EditBookForm("Buch", Collections.emptyList(), LocalDate.now().toString(),
                        "978-3-492-70479-3", -1, Locale.GERMAN.toString(), Collections.emptyList());
                try {
                    management.createBook(form, List.of(mock(Author.class)));
                    fail();
                } catch (IllegalArgumentException ignored) {
                    assertEquals(0, books.size());
                }
            }

            @Test
            void createBookSavesInstanceWhenValid() {
                EditBookForm form;
                Author author = authorRepository.save(new Author("Testo", "Testi", null, null, CountryCode.DE));
                Book book = new Book("Buch", List.of(author), LocalDate.now(), "978-3-492-70479-3", 1, Locale.GERMAN);
                try {
                    Book savedBook = management.createBook(book.getTitle(), book.getAuthors(), book.getPublished(),
                            book.getIsbn(), book.getPages(), book.getLanguage());
                    books.add(savedBook);
                    assertTrue(books.contains(savedBook));
                    assertEquals(book.getTitle(), savedBook.getTitle());
                    assertEquals(book.getAuthors().size(), savedBook.getAuthors().size());
                    assertEquals(book.getAuthors().stream().findAny().orElseThrow(),
                            savedBook.getAuthors().stream().findAny().orElseThrow());
                    assertEquals(book.getPublished(), savedBook.getPublished());
                    assertEquals(book.getIsbn(), savedBook.getIsbn());
                    assertEquals(book.getPages(), savedBook.getPages());
                    assertEquals(book.getLanguage(), savedBook.getLanguage());
                } catch (Exception e) {
                    e.printStackTrace();
                    fail();
                }
                form = new EditBookForm("Buch", Collections.emptyList(), LocalDate.now().toString(),
                        "978-3-492-70479-3", 1, Locale.GERMAN.toString(), Collections.emptyList());
                try {
                    Book savedBook = management.createBook(form, List.of(author));
                    books.add(savedBook);
                    assertTrue(books.contains(savedBook));
                    assertEquals(book.getTitle(), savedBook.getTitle());
                    assertEquals(book.getAuthors().size(), savedBook.getAuthors().size());
                    assertEquals(book.getAuthors().stream().findAny().orElseThrow(),
                            savedBook.getAuthors().stream().findAny().orElseThrow());
                    assertEquals(book.getPublished(), savedBook.getPublished());
                    assertEquals(book.getIsbn(), savedBook.getIsbn());
                    assertEquals(book.getPages(), savedBook.getPages());
                    assertEquals(book.getLanguage(), savedBook.getLanguage());
                } catch (Exception e) {
                    e.printStackTrace();
                    fail();
                }
            }

            @Test
            void createBookAssertsInvalidForm() {
                EditBookForm form;
                form = new EditBookForm("", Collections.emptyList(), LocalDate.now().toString(), "978-3-492-70479-3", 1,
                        Locale.GERMAN.toString(), Collections.emptyList());
                try {
                    management.createBook(form, List.of(mock(Author.class)));
                    fail();
                } catch (IllegalArgumentException ignored) {
                }
                form = new EditBookForm(null, Collections.emptyList(), LocalDate.now().toString(), "978-3-492-70479-3",
                        1, Locale.GERMAN.toString(), Collections.emptyList());
                try {
                    management.createBook(form, List.of(mock(Author.class)));
                    fail();
                } catch (NullPointerException ignored) {
                }
                form = new EditBookForm("Buch", Collections.emptyList(), "", "978-3-492-70479-3", 1,
                        Locale.GERMAN.toString(), Collections.emptyList());
                try {
                    management.createBook(form, List.of(mock(Author.class)));
                    fail();
                } catch (DateTimeParseException ignored) {
                }
                form = new EditBookForm("Buch", Collections.emptyList(), null, "978-3-492-70479-3", 1,
                        Locale.GERMAN.toString(), Collections.emptyList());
                try {
                    management.createBook(form, List.of(mock(Author.class)));
                    fail();
                } catch (NullPointerException ignored) {
                }
                form = new EditBookForm("Buch", Collections.emptyList(), LocalDate.now().toString(), "", 1,
                        Locale.GERMAN.toString(), Collections.emptyList());
                try {
                    management.createBook(form, List.of(mock(Author.class)));
                    fail();
                } catch (IllegalArgumentException ignored) {
                }
                form = new EditBookForm("Buch", Collections.emptyList(), LocalDate.now().toString(), null, 1,
                        Locale.GERMAN.toString(), Collections.emptyList());
                try {
                    management.createBook(form, List.of(mock(Author.class)));
                    fail();
                } catch (NullPointerException ignored) {
                }
                form = new EditBookForm("Buch", Collections.emptyList(), LocalDate.now().toString(),
                        "978-3-492-70479-3", 0, Locale.GERMAN.toString(), Collections.emptyList());
                try {
                    management.createBook(form, List.of(mock(Author.class)));
                    fail();
                } catch (IllegalArgumentException ignored) {
                }
                form = new EditBookForm("Buch", Collections.emptyList(), LocalDate.now().toString(),
                        "978-3-492-70479-3", null, Locale.GERMAN.toString(), Collections.emptyList());
                try {
                    management.createBook(form, List.of(mock(Author.class)));
                    fail();
                } catch (NullPointerException ignored) {
                }
            }
        }

        @Nested
        @SpringBootTest
        @Transactional
        class UpdateBookTests {
            int numberOfTestBooks = 3;
            @Autowired
            AuthorRepository authorRepository;
            @Autowired
            ReadingRepository readingRepository;
            @Autowired
            BookRepository repository;
            BookManagement management;
            Set<Book> testBooks = new HashSet<>();

            @BeforeEach
            void setup() {
                Author author = authorRepository.save(new Author("Testo", "Tester", LocalDate.now(), LocalDate.now(),
                        CountryCode.DE));
                SeriesRepository seriesRepository = mock(SeriesRepository.class);
                when(seriesRepository.findByBooksContains(any())).thenReturn(Streamable.empty());
                management = new BookManagement(repository, seriesRepository, new ReadingManagement(readingRepository));
                for (int i = 1; i <= numberOfTestBooks; i++) {
                    testBooks.add(management.createBook("Buch" + i, List.of(author), LocalDate.now(),
                            "978-3-492-70479-3", 969, Locale.GERMAN));
                }
            }

            @AfterEach
            void tearDown() {
                Set<Author> authors = new HashSet<>();
                for (Book book : testBooks) {
                    authors.addAll(book.getAuthors());
                    management.deleteBook(book);
                }
                testBooks.clear();
                authorRepository.deleteAll(authors);
            }

            @Test
            void updateBookUpdatesAttributes() {
                Author author = authorRepository.save(new Author("Testo2", "Tester", LocalDate.now(), LocalDate.now(),
                        CountryCode.DE));
                for (Book book : testBooks) {
                    String title = book.getTitle();
                    Book updatedBook = management.updateBook(book.getId(), title + title,
                            List.of(book.getAuthors().stream().findAny().orElseThrow(), author),
                            LocalDate.now().minusDays(1), "ISBN 978-3-492-70479-3", 669, Locale.ENGLISH);
                    assertEquals(book.getId(), updatedBook.getId());
                    assertEquals(title + title, updatedBook.getTitle());
                    assertEquals(2, updatedBook.getAuthors().size());
                    assertEquals(LocalDate.now().minusDays(1), updatedBook.getPublished());
                    assertEquals("ISBN 978-3-492-70479-3", updatedBook.getIsbn());
                    assertEquals(669, updatedBook.getPages());
                    assertEquals(Locale.ENGLISH, updatedBook.getLanguage());
                }
            }

            @Test
            void updateBookFormUpdatesAttributes() {
                Author author = authorRepository.save(new Author("Testo2", "Tester", LocalDate.now(), LocalDate.now(),
                        CountryCode.DE));
                for (Book book : testBooks) {
                    String title = book.getTitle();
                    EditBookForm form = new EditBookForm(title + title, List.of(author.getId()),
                            LocalDate.now().minusDays(1).toString(), "ISBN 978-3-492-70479-3", 669,
                            Locale.ENGLISH.toString(), Collections.emptyList());
                    Book updatedBook = management.updateBook(book.getId(), form,
                            List.of(book.getAuthors().stream().findAny().orElseThrow(), author));
                    assertEquals(book.getId(), updatedBook.getId());
                    assertEquals(title + title, updatedBook.getTitle());
                    assertEquals(2, updatedBook.getAuthors().size());
                    assertEquals(LocalDate.now().minusDays(1), updatedBook.getPublished());
                    assertEquals("ISBN 978-3-492-70479-3", updatedBook.getIsbn());
                    assertEquals(669, updatedBook.getPages());
                    assertEquals(Locale.ENGLISH, updatedBook.getLanguage());
                }
            }

            @Test
            void updateBookSavesUpdatedInstance() {
                for (Book book : testBooks) {
                    String title = book.getTitle();
                    management.updateBook(book.getId(), "Titel", book.getAuthors(), book.getPublished(), book.getIsbn(),
                            book.getPages(), book.getLanguage());
                    Book updated = management.findBookById(book.getId());
                    assertNotEquals(title, updated.getTitle());
                    title = updated.getTitle();
                    EditBookForm form = new EditBookForm("Title",
                            book.getAuthors().stream().map(Author::getId).collect(Collectors.toList()),
                            book.getPublished().toString(), book.getIsbn(), book.getPages(),
                            book.getLanguage().toString(), Collections.emptyList());
                    management.updateBook(book.getId(), form, book.getAuthors());
                    assertNotEquals(title, management.findBookById(updated.getId()).getTitle());
                }
            }

            @Test
            void updateBookAssertsInvalidForm() {
                Book book = testBooks.stream().findAny().orElseThrow();
                Set<Author> authors = book.getAuthors();
                EditBookForm form;
                form = new EditBookForm(null, authors.stream().map(Author::getId)
                        .collect(Collectors.toList()), book.getPublished().toString(), book.getIsbn(), book.getPages(),
                        book.getLanguage().toString(), Collections.emptyList());
                try {
                    management.updateBook(book.getId(), form, authors);
                    fail();
                } catch (NullPointerException ignored) {
                    assertNotNull(management.findBookById(book.getId()).getTitle());
                }
                form = new EditBookForm("", authors.stream().map(Author::getId)
                        .collect(Collectors.toList()), book.getPublished().toString(), book.getIsbn(), book.getPages(),
                        book.getLanguage().toString(), Collections.emptyList());
                try {
                    management.updateBook(book.getId(), form, authors);
                    fail();
                } catch (IllegalArgumentException ignored) {
                    assertNotEquals("", management.findBookById(book.getId()).getTitle());
                }
                form = new EditBookForm(book.getTitle(), Collections.emptyList(), book.getPublished().toString(),
                        book.getIsbn(), book.getPages(), book.getLanguage().toString(), Collections.emptyList());
                try {
                    management.updateBook(book.getId(), form, Collections.emptyList());
                    fail();
                } catch (IllegalArgumentException ignored) {
                    assertNotEquals(0, management.findBookById(book.getId()).getAuthors().size());
                }
                form = new EditBookForm(book.getTitle(), authors.stream().map(Author::getId)
                        .collect(Collectors.toList()), null, book.getIsbn(), book.getPages(),
                        book.getLanguage().toString(), Collections.emptyList());
                try {
                    management.updateBook(book.getId(), form, authors);
                    fail();
                } catch (NullPointerException ignored) {
                    assertNotNull(management.findBookById(book.getId()).getPublished());
                }
                form = new EditBookForm(book.getTitle(), authors.stream().map(Author::getId)
                        .collect(Collectors.toList()), "", book.getIsbn(), book.getPages(),
                        book.getLanguage().toString(), Collections.emptyList());
                try {
                    management.updateBook(book.getId(), form, authors);
                    fail();
                } catch (DateTimeParseException ignored) {
                    assertNotEquals("", management.findBookById(book.getId()).getPublished().toString());
                }
                form = new EditBookForm(book.getTitle(), authors.stream().map(Author::getId)
                        .collect(Collectors.toList()), book.getPublished().toString(), null, book.getPages(),
                        book.getLanguage().toString(), Collections.emptyList());
                try {
                    management.updateBook(book.getId(), form, authors);
                    fail();
                } catch (NullPointerException ignored) {
                    assertNotNull(management.findBookById(book.getId()).getIsbn());
                }
                form = new EditBookForm(book.getTitle(), authors.stream().map(Author::getId)
                        .collect(Collectors.toList()), book.getPublished().toString(), "", book.getPages(),
                        book.getLanguage().toString(), Collections.emptyList());
                try {
                    management.updateBook(book.getId(), form, authors);
                    fail();
                } catch (IllegalArgumentException ignored) {
                    assertNotEquals("", management.findBookById(book.getId()).getIsbn());
                }
                form = new EditBookForm(book.getTitle(), authors.stream().map(Author::getId)
                        .collect(Collectors.toList()), book.getPublished().toString(), "book.getIsbn()", book.getPages(),
                        book.getLanguage().toString(), Collections.emptyList());
                try {
                    management.updateBook(book.getId(), form, authors);
                    fail();
                } catch (IllegalArgumentException ignored) {
                    assertNotEquals("book.getIsbn()", management.findBookById(book.getId()).getIsbn());
                }
                form = new EditBookForm(book.getTitle(), authors.stream().map(Author::getId)
                        .collect(Collectors.toList()), book.getPublished().toString(), book.getIsbn(), 0,
                        book.getLanguage().toString(), Collections.emptyList());
                try {
                    management.updateBook(book.getId(), form, authors);
                    fail();
                } catch (IllegalArgumentException ignored) {
                    assertNotEquals(0, management.findBookById(book.getId()).getPages());
                }
                form = new EditBookForm(book.getTitle(), authors.stream().map(Author::getId)
                        .collect(Collectors.toList()), book.getPublished().toString(), book.getIsbn(), -1,
                        book.getLanguage().toString(), Collections.emptyList());
                try {
                    management.updateBook(book.getId(), form, authors);
                    fail();
                } catch (IllegalArgumentException ignored) {
                    assertNotEquals(-1, management.findBookById(book.getId()).getPages());
                }
            }

            @Test
            void updateBookAssertsInvalidId() {
                try {
                    management.updateBook(0, "Title", List.of(mock(Author.class)), LocalDate.now(),
                            "ISBN 978-3-492-70479-3", 69, Locale.GERMAN);
                    fail();
                } catch (NoSuchElementException ignored) {
                }
            }

            @Test
            void updateBookAssertsTitleBlank() {
                Book book = testBooks.stream().findAny().orElseThrow();
                try {
                    management.updateBook(book.getId(), "", List.of(mock(Author.class)), LocalDate.now(),
                            "ISBN 978-3-492-70479-3", 69, Locale.GERMAN);
                    fail();
                } catch (IllegalArgumentException ignored) {
                    assertNotEquals("", management.findBookById(book.getId()).getTitle());
                }
            }

            @Test
            void updateBookAssertsAuthorsEmpty() {
                Book book = testBooks.stream().findAny().orElseThrow();
                try {
                    management.updateBook(book.getId(), "Title", Collections.emptySet(), LocalDate.now(),
                            "ISBN 978-3-492-70479-3", 69, Locale.GERMAN);
                    fail();
                } catch (IllegalArgumentException ignored) {
                    assertNotEquals(0, management.findBookById(book.getId()).getAuthors().size());
                }
            }

            @Test
            void updateBookAssertsPagesZero() {
                Book book = testBooks.stream().findAny().orElseThrow();
                try {
                    management.updateBook(book.getId(), "Title", List.of(mock(Author.class)), LocalDate.now(),
                            "ISBN 978-3-492-70479-3", 0, Locale.GERMAN);
                    fail();
                } catch (IllegalArgumentException ignored) {
                    assertNotEquals(0, management.findBookById(book.getId()).getPages());
                }
            }

            @Test
            void updateBookAssertsPagesNegative() {
                Book book = testBooks.stream().findAny().orElseThrow();
                try {
                    management.updateBook(book.getId(), "Title", List.of(mock(Author.class)), LocalDate.now(),
                            "ISBN 978-3-492-70479-3", -1, Locale.GERMAN);
                    fail();
                } catch (IllegalArgumentException ignored) {
                    assertNotEquals(-1, management.findBookById(book.getId()).getPages());
                }
            }
        }

        @Nested
        @SpringBootTest
        @Transactional
        class DeleteBookTests {
            final int numberOfTestBooks = 3;
            final int numberOfTestSeries = 2;
            final int numberOfTestReadings = 2;
            @Autowired
            AuthorRepository authorRepository;
            @Autowired
            BookRepository repository;
            @Autowired
            SeriesRepository seriesRepository;
            @Autowired
            ReadingManagement readingManagement;
            BookManagement management;
            Set<Book> books = new HashSet<>();
            Set<Series> series = new HashSet<>();
            Set<Reading> readings = new HashSet<>();

            @BeforeEach
            void setup() {
                Author author = authorRepository.save(new Author("Testo", "Tester", null, null, CountryCode.DE));
                management = new BookManagement(repository, seriesRepository, readingManagement);
                for (int i = 0; i < numberOfTestBooks; i++) {
                    Book book = management.createBook("Buch", List.of(author), LocalDate.now(),
                            "ISBN 978-3-492-70479-3", 69, Locale.GERMAN);
                    books.add(book);
                    for (int j = 0; j < numberOfTestReadings; j++) {
                        readings.add(readingManagement.createReading(book, LocalDate.now(), null, 50));
                    }
                }
                for (int i = 0; i < numberOfTestSeries; i++) {
                    series.add(management.createSeries("Series" + i, books));
                }
            }

            @AfterEach
            void tearDown() {
                for (Reading reading : readings) {
                    readingManagement.deleteReading(reading);
                }
                readings.clear();
                Set<Author> authors = new HashSet<>();
                for (Series series : series) {
                    authors.addAll(series.getAuthors());
                    seriesRepository.delete(series);
                }
                series.clear();
                repository.deleteAll(books);
                books.clear();
                authorRepository.deleteAll(authors);
                authors.clear();
            }

            @Test
            void deleteBookDeletesBookById() {
                for (Book book : books) {
                    management.deleteBook(book);
                    try {
                        management.findBookById(book.getId());
                        fail();
                    } catch (NoSuchElementException ignored) {
                    }
                }
            }

            @Test
            void deleteBookDeletesBookByInstance() {
                for (Book book : books) {
                    management.deleteBook(book);
                    try {
                        management.findBookById(book.getId());
                        fail();
                    } catch (NoSuchElementException ignored) {
                    }
                }
            }

            @Test
            void deleteBookDeletesAssociatedReadings() {
                for (Book book : books) {
                    long readingCount = readingManagement.getReadingCount();
                    management.deleteBook(book);
                    assertEquals(readingCount - numberOfTestReadings, readingManagement.getReadingCount());
                }
            }

            @Test
            void deleteBookKeepsAssociatedSeries() {
                for (Book book : books) {
                    long seriesCount = management.getSeriesCount();
                    management.deleteBook(book);
                    assertEquals(seriesCount, management.getSeriesCount());
                }
            }

            @Test
            void deleteBookAssertsInvalidId() {
                try {
                    management.deleteBook(0);
                    fail();
                } catch (NoSuchElementException ignored) {
                }
            }
        }
    }

    @Nested
    class SeriesTest {
        @Nested
        @SpringBootTest
        @Transactional
        class CreateSeriesTests {
            Set<Series> series = new HashSet<>();
            @Autowired
            BookRepository bookRepository;
            @Autowired
            SeriesRepository seriesRepository;
            @Autowired
            BookManagement management;
            @Autowired
            AuthorRepository authorRepository;

            @BeforeEach
            void setup() {
                management = new BookManagement(bookRepository, seriesRepository, mock(ReadingManagement.class));
            }

            @AfterEach
            void tearDown() {
                seriesRepository.deleteAll(series);
                series.clear();
            }

            @Test
            void createSeriesAssertsTitleBlank() {
                EditSeriesForm form;
                try {
                    series.add(management.createSeries("", null));
                    fail();
                } catch (IllegalArgumentException ignored) {
                    assertEquals(0, series.size());
                }
                form = new EditSeriesForm("", null);
                try {
                    series.add(management.createSeries(form));
                    fail();
                } catch (IllegalArgumentException ignored) {
                    assertEquals(0, series.size());
                }
                try {
                    series.add(management.createSeries(" ", null));
                    fail();
                } catch (IllegalArgumentException ignored) {
                    assertEquals(0, series.size());
                }
                form = new EditSeriesForm(" ", null);
                try {
                    series.add(management.createSeries(form));
                    fail();
                } catch (IllegalArgumentException ignored) {
                    assertEquals(0, series.size());
                }
            }

            @Test
            void createSeriesAddsAllBooks() {
                Author author = authorRepository.save(new Author("Testi", "Tester", null, null, CountryCode.DE));
                Book book1 = management.createBook("Titel1", List.of(author), LocalDate.now(), "978-3-492-70479-3", 69,
                        Locale.GERMAN);
                Book book2 = management.createBook("Titel2", List.of(author), LocalDate.now(), "978-3-492-70479-3", 69,
                        Locale.GERMAN);
                Series newSeries = management.createSeries("Series", List.of(book1, book2));
                series.add(newSeries);
                Set<Book> books = management.findSeriesById(newSeries.getId()).getBooks();
                assertEquals(2, books.size());
                assertTrue(books.contains(book1));
                assertTrue(books.contains(book2));
                management.deleteBook(book1);
                management.deleteBook(book2);
            }

            @Test
            void createSeriesAllowsBooksEmpty() {
                try {
                    series.add(management.createSeries("Title", Collections.emptyList()));
                } catch (Exception e) {
                    e.printStackTrace();
                    fail();
                }
            }

            @Test
            void createSeriesAllowsBooksNull() {
                try {
                    series.add(management.createSeries("Title", null));
                } catch (Exception e) {
                    e.printStackTrace();
                    fail();
                }
            }

            @Test
            void createSeriesSavesInstanceWhenValid() {
                EditSeriesForm form;
                Series newSeries = new Series("Series", null);
                try {
                    Series savedSeries = management.createSeries(newSeries.getTitle(), null);
                    series.add(savedSeries);
                    assertTrue(management.findAllSeries().toList().contains(savedSeries));
                    assertEquals(newSeries.getTitle(), savedSeries.getTitle());
                    assertEquals(newSeries.getBooks().size(), savedSeries.getBooks().size());
                    assertEquals(newSeries.getAuthors().size(), savedSeries.getAuthors().size());
                } catch (Exception e) {
                    e.printStackTrace();
                    fail();
                }
                form = new EditSeriesForm(newSeries.getTitle(), null);
                try {
                    Series savedSeries = management.createSeries(form);
                    series.add(savedSeries);
                    assertTrue(management.findAllSeries().toList().contains(savedSeries));
                    assertEquals(newSeries.getTitle(), savedSeries.getTitle());
                    assertEquals(newSeries.getBooks().size(), savedSeries.getBooks().size());
                    assertEquals(newSeries.getAuthors().size(), savedSeries.getAuthors().size());
                } catch (Exception e) {
                    e.printStackTrace();
                    fail();
                }
            }

            @Test
            void createSeriesAssertsInvalidForm() {
                EditSeriesForm form;
                form = new EditSeriesForm("", null);
                try {
                    series.add(management.createSeries(form));
                    fail();
                } catch (IllegalArgumentException ignored) {
                }
                form = new EditSeriesForm(" ", null);
                try {
                    series.add(management.createSeries(form));
                    fail();
                } catch (IllegalArgumentException ignored) {
                }
            }
        }

        @Nested
        @SpringBootTest
        @Transactional
        class UpdateSeriesTests {
            int numberOfTestSeries = 3;
            int numberOfTestBooks = 5;
            @Autowired
            AuthorRepository authorRepository;
            @Autowired
            SeriesRepository seriesRepository;
            @Autowired
            BookRepository bookRepository;
            BookManagement management;
            List<Author> authors = new ArrayList<>();
            List<Book> books = new ArrayList<>();
            Set<Series> series = new HashSet<>();

            @BeforeEach
            void setup() {
                authors.add(authorRepository.save(new Author("Testo", "Tester", null, null, CountryCode.DE)));
                authors.add(authorRepository.save(new Author("Testi", "Tester", null, null, CountryCode.DE)));
                management = new BookManagement(bookRepository, seriesRepository, mock(ReadingManagement.class));
                for (int i = 0; i < numberOfTestBooks; i++) {
                    books.add(management.createBook("Title", authors.subList(i % 2, i % 2 + 1), LocalDate.now(),
                            "978-3-492-70479-3", 69, Locale.GERMAN));
                }
                for (int i = 0; i < numberOfTestSeries; i++) {
                    series.add(management.createSeries("Series",
                            books.subList(i, numberOfTestBooks - numberOfTestSeries + i)));
                }
            }

            @AfterEach
            void tearDown() {
                seriesRepository.deleteAll(series);
                series.clear();
                bookRepository.deleteAll(books);
                books.clear();
                authorRepository.deleteAll(authors);
                authors.clear();
            }

            @Test
            void updateSeriesUpdatesAttributes() {
                for (Series series : series) {
                    SortedSet<Book> books = series.getBooks();
                    Series updateSeries = management.updateSeries(series.getId(), "Changed", null);
                    assertEquals("Changed", updateSeries.getTitle());
                    assertEquals(0, updateSeries.getBooks().size());
                }
            }

            @Test
            void updateSeriesFormUpdatesAttributes() {
                for (Series series : series) {
                    SortedSet<Book> books = series.getBooks();
                    EditSeriesForm form = new EditSeriesForm("Changed", null);
                    Series updateSeries = management.updateSeries(series.getId(), form);
                    assertEquals("Changed", updateSeries.getTitle());
                    assertEquals(0, updateSeries.getBooks().size());
                }
            }

            @Test
            void updateSeriesSavesUpdatedInstance() {
                for (Series series : series) {
                    String title = series.getTitle();
                    management.updateSeries(series.getId(), "Title", null);
                    Series updated = management.findSeriesById(series.getId());
                    assertNotEquals(title, updated.getTitle());
                    assertEquals(0, updated.getBooks().size());
                    EditSeriesForm form = new EditSeriesForm(title,
                            series.getBooks().stream().map(Book::getId).collect(Collectors.toList()));
                    management.updateSeries(series.getId(), form);
                    assertEquals(title, management.findSeriesById(series.getId()).getTitle());
                    assertEquals(series.getBooks().size(), management.findSeriesById(series.getId()).getBooks().size());
                }
            }

            @Test
            void updateSeriesAssertsInvalidForm() {
                Series series = management.findAllSeries().stream().findAny().orElseThrow();
                EditSeriesForm form;
                form = new EditSeriesForm(null, null);
                try {
                    management.updateSeries(series.getId(), form);
                    fail();
                } catch (NullPointerException ignored) {
                    assertEquals(series.getTitle(), management.findSeriesById(series.getId()).getTitle());
                }
                form = new EditSeriesForm("", null);
                try {
                    management.updateSeries(series.getId(), form);
                    fail();
                } catch (IllegalArgumentException ignored) {
                    assertEquals(series.getTitle(), management.findSeriesById(series.getId()).getTitle());
                }
                form = new EditSeriesForm(" ", null);
                try {
                    management.updateSeries(series.getId(), form);
                    fail();
                } catch (IllegalArgumentException ignored) {
                    assertEquals(series.getTitle(), management.findSeriesById(series.getId()).getTitle());
                }
            }

            @Test
            void updateSeriesAssertsInvalidId() {
                try {
                    management.updateSeries(0, "Title", null);
                    fail();
                } catch (NoSuchElementException ignored) {
                }
            }

            @Test
            void updateSeriesAssertsTitleBlank() {
                Series series = management.findAllSeries().stream().findAny().orElseThrow();
                try {
                    management.updateSeries(series.getId(), " ", null);
                    fail();
                } catch (IllegalArgumentException ignored) {
                    assertEquals(series.getTitle(), management.findSeriesById(series.getId()).getTitle());
                }
            }
        }

        @Nested
        @SpringBootTest
        class DeleteSeriesTests {
            int numberOfTestSeries = 3;
            int numberOfTestBooks = 5;
            @Autowired
            AuthorRepository authorRepository;
            @Autowired
            SeriesRepository seriesRepository;
            @Autowired
            BookRepository bookRepository;
            BookManagement management;
            List<Author> authors = new ArrayList<>();
            List<Book> books = new ArrayList<>();
            Set<Series> series = new HashSet<>();

            @BeforeEach
            void setup() {
                authors.add(authorRepository.save(new Author("Testo", "Tester", null, null, CountryCode.DE)));
                authors.add(authorRepository.save(new Author("Testi", "Tester", null, null, CountryCode.DE)));
                management = new BookManagement(bookRepository, seriesRepository, mock(ReadingManagement.class));
                for (int i = 0; i < numberOfTestBooks; i++) {
                    books.add(management.createBook("Title", authors.subList(i % 2, i % 2 + 1), LocalDate.now(),
                            "978-3-492-70479-3", 69, Locale.GERMAN));
                }
                for (int i = 0; i < numberOfTestSeries; i++) {
                    series.add(management.createSeries("Series",
                            books.subList(i, numberOfTestBooks - numberOfTestSeries + i)));
                }
            }

            @AfterEach
            void tearDown() {
                seriesRepository.deleteAll(series);
                series.clear();
                bookRepository.deleteAll(books);
                books.clear();
                authorRepository.deleteAll(authors);
                authors.clear();
            }

            @Test
            void deleteSeriesDeletesSeries() {
                for (Series series : series) {
                    management.deleteSeries(series.getId());
                    try {
                        management.findSeriesById(series.getId());
                        fail();
                    } catch (NoSuchElementException ignored) {
                    }
                }
            }

            @Test
            void deleteSeriesKeepsAssociatedBooks() {
                for (Series series : series) {
                    long bookCount = management.getBookCount();
                    management.deleteSeries(series.getId());
                    assertEquals(bookCount, management.getBookCount());
                }
            }

            @Test
            void deleteSeriesAssertsInvalidId() {
                try {
                    management.deleteSeries(0);
                    fail();
                } catch (EmptyResultDataAccessException ignored) {
                }
            }
        }

        @Nested
        @SpringBootTest
        @Transactional
        class UpdateElementsTest {
            int numberOfTestSeries = 3;
            int numberOfTestBooks = 5;
            @Autowired
            AuthorRepository authorRepository;
            @Autowired
            SeriesRepository seriesRepository;
            @Autowired
            BookRepository bookRepository;
            BookManagement management;
            List<Author> authors = new ArrayList<>();
            List<Book> books = new ArrayList<>();
            Set<Series> series = new HashSet<>();

            @BeforeEach
            void setup() {
                authors.add(authorRepository.save(new Author("Testo", "Tester", null, null, CountryCode.DE)));
                authors.add(authorRepository.save(new Author("Testi", "Tester", null, null, CountryCode.DE)));
                management = new BookManagement(bookRepository, seriesRepository, mock(ReadingManagement.class));
                for (int i = 0; i < numberOfTestBooks; i++) {
                    books.add(management.createBook("Title" + i, authors.subList(i % 2, i % 2 + 1), LocalDate.now(),
                            "978-3-492-70479-3", 69, Locale.GERMAN));
                }
                for (int i = 0; i < numberOfTestSeries; i++) {
                    series.add(management.createSeries("Series" + i,
                            books.subList(i, numberOfTestBooks - numberOfTestSeries + i + 1)));
                }
            }

            @AfterEach
            void tearDown() {
                seriesRepository.deleteAll(series);
                series.clear();
                bookRepository.deleteAll(books);
                books.clear();
                authorRepository.deleteAll(authors);
                authors.clear();
            }

            @Test
            void addBooksToSeriesAddsAllBooks() {
                for (Series series : series) {
                    for (Book book : books) {
                        management.addBooksToSeries(book, series.getId());
                        assertTrue(management.findSeriesById(series.getId()).getBooks().contains(book));
                    }
                    assertTrue(series.getBooks().containsAll(books));
                }
            }

            @Test
            void addBooksToSeriesAssertsInvalidId() {
                try {
                    management.addBooksToSeries(books, 0);
                    fail();
                } catch (NoSuchElementException ignored) {
                }
            }

            @Test
            void addBooksToSeriesAllowsBooksNull() {
                Series series = this.series.stream().findAny().orElseThrow();
                Set<Book> books = series.getBooks();
                try {
                    management.addBooksToSeries((Collection<Book>) null, series.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                    fail();
                }
                assertEquals(books.size(), management.findSeriesById(series.getId()).getBooks().size());
            }

            @Test
            void addBooksToSeriesAllowsBooksEmpty() {
                Series series = this.series.stream().findAny().orElseThrow();
                Set<Book> books = series.getBooks();
                try {
                    management.addBooksToSeries(Collections.emptyList(), series.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                    fail();
                }
                assertEquals(books.size(), management.findSeriesById(series.getId()).getBooks().size());
            }

            @Test
            void removeBookFromAllSeriesRemovesBookFromAllSeries() {
                for (Book book : books) {
                    management.removeBookFromAllSeries(book);
                    for (Series series : series) {
                        assertFalse(management.findSeriesById(series.getId()).getBooks().contains(book));
                    }
                }
            }
        }
    }
}
