package com.peternaggschga.books.books;

import com.neovisionaries.i18n.CountryCode;
import com.peternaggschga.books.author.Author;
import com.peternaggschga.books.author.AuthorRepository;
import com.peternaggschga.books.books.book.Book;
import com.peternaggschga.books.books.book.BookRepository;
import com.peternaggschga.books.books.book.EditBookForm;
import com.peternaggschga.books.books.series.Series;
import com.peternaggschga.books.books.series.SeriesRepository;
import com.peternaggschga.books.reading.Reading;
import com.peternaggschga.books.reading.ReadingManagement;
import com.peternaggschga.books.reading.ReadingRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
        class CreateBookTests {
            Set<Book> books = new HashSet<>();
            BookManagement management;

            @BeforeEach
            void setup() {
                books.clear();
                BookRepository repository = mock(BookRepository.class);
                when(repository.save(any())).thenAnswer(invocationOnMock -> {
                    books.add(invocationOnMock.getArgument(0));
                    return invocationOnMock.getArgument(0);
                });
                management = new BookManagement(repository, mock(SeriesRepository.class),
                        mock(ReadingManagement.class));
            }

            @Test
            void createBookAssertsTitleBlank() {
                EditBookForm form;
                try {
                    management.createBook("", List.of(mock(Author.class)), LocalDate.now(), "978-3-492-70479-3", 1,
                            Locale.GERMAN);
                    fail();
                } catch (IllegalArgumentException ignored) {
                    assertEquals(0, books.size());
                }
                form = new EditBookForm("", List.of(1L), LocalDate.now().toString(), "978-3-492-70479-3", 1,
                        Locale.GERMAN.toString(), new ArrayList<>());
                try {
                    management.createBook(form, List.of(mock(Author.class)));
                    fail();
                } catch (IllegalArgumentException ignored) {
                    assertEquals(0, books.size());
                }
                try {
                    management.createBook(" ", List.of(mock(Author.class)), LocalDate.now(), "978-3-492-70479-3", 1,
                            Locale.GERMAN);
                    fail();
                } catch (IllegalArgumentException ignored) {
                    assertEquals(0, books.size());
                }
                form = new EditBookForm(" ", List.of(1L), LocalDate.now().toString(), "978-3-492-70479-3", 1,
                        Locale.GERMAN.toString(), new ArrayList<>());
                try {
                    management.createBook(form, List.of(mock(Author.class)));
                    fail();
                } catch (IllegalArgumentException ignored) {
                    assertEquals(0, books.size());
                }
            }

            @Test
            void createBookAssertsAuthorsEmpty() {
                EditBookForm form;
                try {
                    management.createBook("Buch", new ArrayList<>(), LocalDate.now(), "978-3-492-70479-3", 1,
                            Locale.GERMAN);
                    fail();
                } catch (IllegalArgumentException ignored) {
                    assertEquals(0, books.size());
                }
                form = new EditBookForm("Buch", new ArrayList<>(), LocalDate.now().toString(), "978-3-492-70479-3", 1,
                        Locale.GERMAN.toString(), new ArrayList<>());
                try {
                    management.createBook(form, new ArrayList<>());
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
                form = new EditBookForm("Buch", new ArrayList<>(), LocalDate.now().toString(), "978-3-492-70479-3", 0,
                        Locale.GERMAN.toString(), new ArrayList<>());
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
                form = new EditBookForm("Buch", new ArrayList<>(), LocalDate.now().toString(), "978-3-492-70479-3", -1,
                        Locale.GERMAN.toString(), new ArrayList<>());
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
                Book book = new Book("Buch", List.of(mock(Author.class)), LocalDate.now(), "978-3-492-70479-3", 1,
                        Locale.GERMAN);
                try {
                    Book savedBook = management.createBook(book.getTitle(), book.getAuthors(), book.getPublished(),
                            book.getIsbn(), book.getPages(), book.getLanguage());
                    assertEquals(1, books.size());
                    assertTrue(books.contains(savedBook));
                    assertEquals(book.getTitle(), savedBook.getTitle());
                    assertEquals(book.getAuthors().size(), savedBook.getAuthors().size());
                    assertEquals(book.getPublished(), savedBook.getPublished());
                    assertEquals(book.getIsbn(), savedBook.getIsbn());
                    assertEquals(book.getPages(), savedBook.getPages());
                    assertEquals(book.getLanguage(), savedBook.getLanguage());
                } catch (Exception e) {
                    e.printStackTrace();
                    fail();
                }
                books.clear();
                form = new EditBookForm("Buch", new ArrayList<>(), LocalDate.now().toString(), "978-3-492-70479-3", 1,
                        Locale.GERMAN.toString(), new ArrayList<>());
                try {
                    Book savedBook = management.createBook(form, List.of(mock(Author.class)));
                    assertEquals(1, books.size());
                    assertTrue(books.contains(savedBook));
                    assertEquals(book.getTitle(), savedBook.getTitle());
                    assertEquals(book.getAuthors().size(), savedBook.getAuthors().size());
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
                form = new EditBookForm("", new ArrayList<>(), LocalDate.now().toString(), "978-3-492-70479-3", 1,
                        Locale.GERMAN.toString(), new ArrayList<>());
                try {
                    management.createBook(form, List.of(mock(Author.class)));
                    fail();
                } catch (IllegalArgumentException ignored) {
                }
                form = new EditBookForm(null, new ArrayList<>(), LocalDate.now().toString(), "978-3-492-70479-3", 1,
                        Locale.GERMAN.toString(), new ArrayList<>());
                try {
                    management.createBook(form, List.of(mock(Author.class)));
                    fail();
                } catch (NullPointerException ignored) {
                }
                form = new EditBookForm("Buch", new ArrayList<>(), "", "978-3-492-70479-3", 1, Locale.GERMAN.toString(),
                        new ArrayList<>());
                try {
                    management.createBook(form, List.of(mock(Author.class)));
                    fail();
                } catch (DateTimeParseException ignored) {
                }
                form = new EditBookForm("Buch", new ArrayList<>(), null, "978-3-492-70479-3", 1,
                        Locale.GERMAN.toString(), new ArrayList<>());
                try {
                    management.createBook(form, List.of(mock(Author.class)));
                    fail();
                } catch (NullPointerException ignored) {
                }
                form = new EditBookForm("Buch", new ArrayList<>(), LocalDate.now().toString(), "", 1,
                        Locale.GERMAN.toString(), new ArrayList<>());
                try {
                    management.createBook(form, List.of(mock(Author.class)));
                    fail();
                } catch (IllegalArgumentException ignored) {
                }
                form = new EditBookForm("Buch", new ArrayList<>(), LocalDate.now().toString(), null, 1,
                        Locale.GERMAN.toString(), new ArrayList<>());
                try {
                    management.createBook(form, List.of(mock(Author.class)));
                    fail();
                } catch (NullPointerException ignored) {
                }
                form = new EditBookForm("Buch", new ArrayList<>(), LocalDate.now().toString(), "978-3-492-70479-3", 0,
                        Locale.GERMAN.toString(), new ArrayList<>());
                try {
                    management.createBook(form, List.of(mock(Author.class)));
                    fail();
                } catch (IllegalArgumentException ignored) {
                }
                form = new EditBookForm("Buch", new ArrayList<>(), LocalDate.now().toString(), "978-3-492-70479-3",
                        null, Locale.GERMAN.toString(), new ArrayList<>());
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
            long numberOfTestBooks = 3;
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
                for (long i = 1; i <= numberOfTestBooks; i++) {
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
}
