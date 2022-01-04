package com.peternaggschga.books.books.series;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EditSeriesFormUnitTest {

    @BeforeAll
    static void canBeInstantiated() {
        try {
            new EditSeriesForm(null, null);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Nested
    class GetterTests {

        @Test
        void getBooksNeverNull() {
            EditSeriesForm form = new EditSeriesForm(null, null);
            assertNotNull(form.getBooks());
            assertEquals(0, form.getBooks().size());
            form = new EditSeriesForm(null, new ArrayList<>());
            assertNotNull(form.getBooks());
            assertEquals(0, form.getBooks().size());
        }
    }

    @Nested
    class SetterTests {
        EditSeriesForm form;

        @BeforeEach
        void setup() {
            form = new EditSeriesForm(null, null);
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
    }
}
