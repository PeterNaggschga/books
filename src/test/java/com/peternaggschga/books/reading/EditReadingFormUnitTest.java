package com.peternaggschga.books.reading;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
public class EditReadingFormUnitTest {

    @BeforeAll
    static void canBeInstantiated() {
        try {
            new EditReadingForm(null, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Nested
    class GetterTests {

        @Test
        void getEndReturnsNull() {
            EditReadingForm form = new EditReadingForm(null, null, "", null);
            assertNull(form.getEnd());
        }
    }

    @Nested
    class SetterTests {
        EditReadingForm form;

        @BeforeEach
        void setup() {
            form = new EditReadingForm(null, null, null, null);
        }

        @Test
        void setBeginningStringAssertsBlank() {
            try {
                form.setBeginningString("");
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            assertNull(form.getBeginningString());
            try {
                form.setBeginningString(" ");
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            assertNull(form.getBeginningString());
        }

        @Test
        void setBeginningStringAssertsFormat() {
            for (String date :
                    List.of("123-12-12", "2004-13-12", "2004-00-12", "2004-20-12", "2004-12-00", "2004-12-32")) {
                try {
                    form.setBeginningString(date);
                    fail();
                } catch (IllegalArgumentException ignored) {
                }
                assertNull(form.getBeginningString());
            }
        }

        @Test
        void setEndStringAssertsBlank() {
            try {
                form.setEndString("");
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            assertNull(form.getEndString());
            try {
                form.setEndString(" ");
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            assertNull(form.getEndString());
        }

        @Test
        void setEndStringAssertsFormat() {
            for (String date :
                    List.of("123-12-12", "2004-13-12", "2004-00-12", "2004-20-12", "2004-12-00", "2004-12-32")) {
                try {
                    form.setEndString(date);
                    fail();
                } catch (IllegalArgumentException ignored) {
                }
                assertNull(form.getEndString());
            }
        }

        @Test
        void setPagesPerHourAssertsZero() {
            try {
                form.setPagesPerHour(0);
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            assertNull(form.getPagesPerHour());
        }

        @Test
        void setPagesPerHourAssertsNegative() {
            try {
                form.setPagesPerHour(-1);
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            assertNull(form.getPagesPerHour());
        }
    }
}
