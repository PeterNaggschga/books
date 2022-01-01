package com.peternaggschga.books.books.book;

import com.peternaggschga.books.author.Author;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = Book.class)
public class BookUnitTest {
    static final String TITLE = "Der Weg der Könige";
    static final Author AUTHOR = mock(Author.class);
    static final List<Author> AUTHORS = List.of(AUTHOR);
    static final LocalDate PUBLISHED = LocalDate.of(2011, 4, 25);
    static final String ISBN = "3453267176";
    static final int PAGES = 896;
    static final Locale LANGUAGE = Locale.GERMAN;

    @BeforeAll
    static void canBeInstantiated() {
        try {
            new Book(TITLE, AUTHORS, PUBLISHED, ISBN, PAGES, LANGUAGE);
            new Book(TITLE, AUTHOR, PUBLISHED, ISBN, PAGES, LANGUAGE);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Nested
    class ConstructorTests {

        @Test
        void constructorsAssertsTitleBlank() {
            try {
                new Book(" ", AUTHORS, PUBLISHED, ISBN, PAGES, LANGUAGE);
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            try {
                new Book("", AUTHORS, PUBLISHED, ISBN, PAGES, LANGUAGE);
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            try {
                new Book(" ", AUTHOR, PUBLISHED, ISBN, PAGES, LANGUAGE);
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            try {
                new Book("", AUTHOR, PUBLISHED, ISBN, PAGES, LANGUAGE);
                fail();
            } catch (IllegalArgumentException ignored) {
            }
        }

        @Test
        void constructorsTrimsStrings() {
            Book book = new Book(" " + TITLE + " ", AUTHORS, PUBLISHED, " " + ISBN + " ", PAGES, LANGUAGE);
            assertEquals(TITLE, book.getTitle());
            assertEquals(ISBN, book.getIsbn());
            book = new Book(" " + TITLE + " ", AUTHOR, PUBLISHED, " " + ISBN + " ", PAGES, LANGUAGE);
            assertEquals(TITLE, book.getTitle());
            assertEquals(ISBN, book.getIsbn());
        }

        @Test
        void constructorsAssertsAuthorsEmpty() {
            try {
                new Book(TITLE, new ArrayList<>(), PUBLISHED, ISBN, PAGES, LANGUAGE);
                fail();
            } catch (IllegalArgumentException ignored) {
            }
        }

        @Test
        void constructorsAssertsNegativePages() {
            try {
                new Book(TITLE, AUTHORS, PUBLISHED, ISBN, -1, LANGUAGE);
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            try {
                new Book(TITLE, AUTHOR, PUBLISHED, ISBN, -1, LANGUAGE);
                fail();
            } catch (IllegalArgumentException ignored) {
            }
        }

        @Test
        void constructorsAssertsZeroPages() {
            try {
                new Book(TITLE, AUTHORS, PUBLISHED, ISBN, 0, LANGUAGE);
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            try {
                new Book(TITLE, AUTHOR, PUBLISHED, ISBN, 0, LANGUAGE);
                fail();
            } catch (IllegalArgumentException ignored) {
            }
        }
    }

    @Nested
    class SetterTests {
        Book book;

        @BeforeEach
        void setup() {
            book = new Book(TITLE, AUTHOR, PUBLISHED, ISBN, PAGES, LANGUAGE);
        }

        @Test
        void setTitleAssertsBlank() {
            try {
                book.setTitle(" ");
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            try {
                book.setTitle("");
                fail();
            } catch (IllegalArgumentException ignored) {
            }
        }

        @Test
        void settersTrimStrings() {
            book.setTitle(" " + TITLE + " ");
            book.setIsbn(" " + ISBN + " ");
            assertEquals(TITLE, book.getTitle());
            assertEquals(ISBN, book.getIsbn());
        }

        @Test
        void setAuthorsAssertsEmpty() {
            try {
                book.setAuthors(new ArrayList<>());
                fail();
            } catch (IllegalArgumentException ignored) {
            }
        }

        @Test
        void setPagesAssertsNegative() {
            try {
                book.setPages(-1);
                fail();
            } catch (IllegalArgumentException ignored) {
            }
        }

        @Test
        void setPagesAssertsZero() {
            try {
                book.setPages(0);
                fail();
            } catch (IllegalArgumentException ignored) {
            }
        }
    }

    @Nested
    class GetterTests {
        Book book;
        Author author;

        @BeforeEach
        void setup() {
            author = mock(Author.class);
            when(author.toString()).thenReturn("Zweiter Autor");
            when(AUTHOR.toString()).thenReturn("Brandon Sanderson");
            book = new Book(TITLE, List.of(AUTHOR, author), PUBLISHED, ISBN, PAGES, LANGUAGE);
        }

        @Test
        void getAuthorStringReturnsMultipleAuthors() {
            assertTrue(book.getAuthorString().equals(author.toString() + ", " + AUTHOR.toString())
                    || book.getAuthorString().equals(AUTHOR + ", " + author.toString()));
        }

        @Test
        void getAuthorStringReturnsSingleAuthor() {
            book.setAuthors(AUTHOR);
            assertEquals(AUTHOR.toString(), book.getAuthorString());
        }
    }
}
