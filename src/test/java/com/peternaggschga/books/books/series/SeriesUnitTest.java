package com.peternaggschga.books.books.series;

import com.neovisionaries.i18n.CountryCode;
import com.peternaggschga.books.author.Author;
import com.peternaggschga.books.books.book.Book;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.SortedSet;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Series.class)
public class SeriesUnitTest {
    static final String TITLE = "Das Rad der Zeit";
    static final List<Author> AUTHORS = new ArrayList<>();
    static final List<Book> BOOKS = new ArrayList<>();

    @BeforeAll
    static void setup() {
        AUTHORS.add(new Author("Robert", "Jordan", LocalDate.of(1948, 10, 17),
                LocalDate.of(2007, 9, 16), CountryCode.US));
        AUTHORS.add(new Author("Brandon", "Sanderson", LocalDate.of(1975, 12, 17),
                null, CountryCode.US));
        BOOKS.add(new Book("Das Auge der Welt", AUTHORS.get(0), LocalDate.of(1990, 1, 15),
                "978-3-492-70711-4", 896, Locale.GERMAN));
        BOOKS.add(new Book("Die Jagd beginnt", AUTHORS.get(0), LocalDate.of(1990, 11, 15),
                "978-3-492-70712-1", 784, Locale.GERMAN));
        BOOKS.add(new Book("Sturm der Finsternis", AUTHORS, LocalDate.of(2009, 10, 27),
                "978-3-492-70722-0", 896, Locale.GERMAN));
    }

    @BeforeAll
    static void canBeInstantiated() {
        try {
            new Series(TITLE, BOOKS);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Nested
    class ConstructorTests {

        @Test
        void constructorAssertsTitleNull() {
            try {
                new Series(null, BOOKS);
                fail();
            } catch (NullPointerException ignored) {
            }
        }

        @Test
        void constructorAssertsTitleBlank() {
            try {
                new Series(" ", BOOKS);
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            try {
                new Series("", BOOKS);
                fail();
            } catch (IllegalArgumentException ignored) {
            }
        }

        @Test
        void constructorTrimsTitle() {
            Series series = new Series(" " + TITLE + " ", BOOKS);
            assertEquals(TITLE, series.getTitle());
        }

        @Test
        void constructorAllowsBooksNull() {
            try {
                Series series = new Series(TITLE, null);
                assertEquals(0, series.getBooks().size());
                assertEquals(0, series.getAuthors().size());
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        void constructorAllowsBooksEmpty() {
            try {
                Series series = new Series(TITLE, new ArrayList<>());
                assertEquals(0, series.getBooks().size());
                assertEquals(0, series.getAuthors().size());
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        void constructorAddsAllBooks() {
            Series series = new Series(TITLE, BOOKS);
            assertEquals(BOOKS.size(), series.getBooks().size());
        }

        @Test
        void constructorAddsAllAuthors() {
            Series series = new Series(TITLE, BOOKS);
            assertEquals(AUTHORS.size(), series.getAuthors().size());
        }
    }

    @Nested
    class GetterTests {
        Series series;

        @BeforeEach
        void setup() {
            series = new Series(TITLE, BOOKS);
        }

        @Test
        void getBooksReturnsSortedCollection() {
            series.remove(BOOKS.get(0));
            series.addAll(List.of(BOOKS.get(0)));
            SortedSet<Book> books = series.getBooks();
            for (int i = 0; i < books.size(); i++) {
                assertEquals(BOOKS.get(i), books.first());
                books.remove(books.first());
            }
        }

        @Test
        void getAuthorsReturnsSortedCollection() {
            SortedSet<Author> authors = series.getAuthors();
            for (int i = 0; i < authors.size(); i++) {
                assertEquals(AUTHORS.get(i), authors.first());
                authors.remove(authors.first());
            }
        }

        @Test
        void getAuthorsReturnsAllAuthors() {
            assertEquals(AUTHORS.size(), series.getAuthors().size());
            series.remove(BOOKS.get(2));
            assertEquals(AUTHORS.size() - 1, series.getAuthors().size());
            series.remove(BOOKS.get(0));
            assertEquals(AUTHORS.size() - 1, series.getAuthors().size());
            series.clear();
            assertEquals(0, series.getAuthors().size());
        }

        @Test
        void getAuthorsReturnsNullWhenEmpty() {
            series.clear();
            assertNull(series.getAuthorString());
        }

        @Test
        void getAuthorsReturnsAuthorToStringWhenSingleAuthor() {
            series.remove(BOOKS.get(2));
            assertEquals(AUTHORS.get(0).toString(), series.getAuthorString());
        }

        @Test
        void getAuthorsReturnsMultipleAuthorsSorted() {
            String name1 = AUTHORS.get(0).toString();
            String name2 = AUTHORS.get(1).toString();
            assertEquals(name1 + ", " + name2, series.getAuthorString());

        }
    }

    @SuppressWarnings("ConstantConditions")
    @Nested
    class SetterTests {
        Series series;

        @BeforeEach
        void setup() {
            series = new Series(TITLE, BOOKS);
        }

        @Test
        void setTitleAssertsNull() {
            try {
                series.setTitle(null);
                fail();
            } catch (NullPointerException ignored) {
            }
            assertEquals(TITLE, series.getTitle());
        }

        @Test
        void setTitleAssertsBlank() {
            try {
                series.setTitle(" ");
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            assertEquals(TITLE, series.getTitle());
            try {
                series.setTitle("");
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            assertEquals(TITLE, series.getTitle());
        }

        @Test
        void setTitleTrimsString() {
            series.setTitle(" " + TITLE + " ");
            assertEquals(TITLE, series.getTitle());
        }
    }

    @Nested
    class UpdateElementsTest {
        Series series;

        @BeforeEach
        void setup() {
            series = new Series(TITLE, null);
        }

        @Test
        void addAllAddsAllBooks() {
            assertEquals(0, series.getBooks().size());
            series.addAll(BOOKS);
            assertEquals(BOOKS.size(), series.getBooks().size());
        }

        @Test
        void addAllReturnsChanged() {
            assertTrue(series.addAll(BOOKS));
            assertFalse(series.addAll(BOOKS));
        }

        @Test
        void addAllAllowsNull() {
            try {
                assertFalse(series.addAll(null));
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
            assertEquals(0, series.getBooks().size());
        }

        @Test
        void removeAllowsNull() {
            try {
                assertFalse(series.remove(null));
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
            assertEquals(0, series.getBooks().size());
        }

        @Test
        void removeRemovesBook() {
            series.addAll(BOOKS);
            for (int i = 0; i < BOOKS.size(); i++) {
                Book book = series.getBooks().first();
                series.remove(book);
                assertFalse(series.getBooks().contains(book));
            }
        }

        @Test
        void clearRemovesAllBooks() {
            series.clear();
            assertEquals(0, series.getBooks().size());
            series.addAll(BOOKS);
            series.clear();
            assertEquals(0, series.getBooks().size());
        }
    }
}
