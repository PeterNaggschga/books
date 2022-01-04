package com.peternaggschga.books.books.book;

import com.peternaggschga.books.books.BookManagement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EditBookFormUnitTest {

    @BeforeAll
    static void canBeInstantiated() {
        try {
            new EditBookForm(null, null, null, null, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Nested
    class GetterTests {

        @Test
        void getLanguageReturnsCorrectValue() {
            for (Locale language : BookManagement.LANGUAGES) {
                EditBookForm form = new EditBookForm(null, null, null, null, null, language.toString(), null);
                assertEquals(language, form.getLanguage());
            }
        }

        @Test
        void getSeriesNeverNull() {
            EditBookForm form = new EditBookForm(null, null, null, null, null, null, null);
            assertNotNull(form.getSeries());
            assertEquals(0, form.getSeries().size());
            form = new EditBookForm(null, null, null, null, null, null, new ArrayList<>());
            assertNotNull(form.getSeries());
            assertEquals(0, form.getSeries().size());
        }
    }

    @Nested
    class SetterTests {
        EditBookForm form;

        @BeforeEach
        void setup() {
            form = new EditBookForm(null, null, null, null, null, null, null);
        }

        @Test
        void setTitleAssertsBlank() {
            try {
                form.setTitle("");
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            assertNull(form.getTitle());
            try {
                form.setTitle(" ");
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            assertNull(form.getTitle());
        }

        @Test
        void setPublishedStringAssertsBlank() {
            try {
                form.setPublishedString("");
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            assertNull(form.getPublishedString());
            try {
                form.setPublishedString(" ");
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            assertNull(form.getPublishedString());
        }

        @Test
        void setPublishedStringAssertsFormat() {
            for (String date :
                    List.of("123-12-12", "2004-13-12", "2004-00-12", "2004-20-12", "2004-12-00", "2004-12-32")) {
                try {
                    form.setPublishedString(date);
                    fail();
                } catch (IllegalArgumentException ignored) {
                }
                assertNull(form.getPublishedString());
            }
        }

        @Test
        void setIsbnAssertsBlank() {
            try {
                form.setIsbn("");
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            assertNull(form.getIsbn());
            try {
                form.setIsbn(" ");
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            assertNull(form.getIsbn());
        }

        @Test
        void setIsbnAssertsFormat() {
            for (String isbn : List.of("ISBN", "ISBN 10", "2004-00-12", "57917981289", "123213")) {
                try {
                    form.setIsbn(isbn);
                    fail();
                } catch (IllegalArgumentException ignored) {
                }
                assertNull(form.getIsbn());
            }
        }

        @Test
        void setPagesAssertsZero() {
            try {
                form.setPages(0);
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            assertNull(form.getPages());
        }

        @Test
        void setPagesAssertsNegative() {
            try {
                form.setPages(-1);
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            assertNull(form.getPages());
        }

        @Test
        void setLanguageStringAssertsBlank() {
            try {
                form.setLanguageString("");
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            assertNull(form.getLanguageString());
            try {
                form.setLanguageString(" ");
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            assertNull(form.getLanguageString());
        }

        @Test
        void setLanguageStringAssertsFormat() {
            try {
                form.setLanguageString("german");
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            assertNull(form.getLanguageString());
            for (Locale language : BookManagement.LANGUAGES) {
                form.setLanguageString(language.toString());
                assertEquals(language.toString(), form.getLanguageString());
            }
        }

        @Test
        void setAuthorsAssertsEmpty() {
            try {
                form.setAuthors(new ArrayList<>());
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            assertNull(form.getAuthors());
        }
    }
}
