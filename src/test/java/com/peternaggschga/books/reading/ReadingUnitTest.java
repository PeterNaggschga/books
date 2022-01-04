package com.peternaggschga.books.reading;

import com.peternaggschga.books.books.book.Book;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@SpringBootTest(classes = Reading.class)
public class ReadingUnitTest {
    static final Book BOOK = mock(Book.class);
    static final LocalDate BEGINNING = LocalDate.of(2020, 10, 10);
    static final LocalDate END = LocalDate.of(2020, 11, 10);
    static final int PAGES_PER_HOUR = 50;

    @BeforeAll
    static void canBeInstantiated() {
        try {
            new Reading(BOOK, BEGINNING, END, PAGES_PER_HOUR);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Nested
    class ConstructorTests {

        @Test
        void constructorAssertsBeginningAfterEnd() {
            try {
                new Reading(BOOK, END, BEGINNING, PAGES_PER_HOUR);
                fail();
            } catch (IllegalArgumentException ignored) {
            }
        }

        @Test
        void constructorAllowsEndNull() {
            try {
                new Reading(BOOK, BEGINNING, null, PAGES_PER_HOUR);
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        void constructorAssertsZeroPagesPerHour() {
            try {
                new Reading(BOOK, END, BEGINNING, 0);
                fail();
            } catch (IllegalArgumentException ignored) {
            }
        }

        @Test
        void constructorAssertsNegativePagesPerHour() {
            try {
                new Reading(BOOK, END, BEGINNING, -1);
                fail();
            } catch (IllegalArgumentException ignored) {
            }
        }
    }

    @Nested
    class GetterTests {

        @Test
        void getEndStringReturnsNull() {
            Reading reading = new Reading(BOOK, BEGINNING, null, PAGES_PER_HOUR);
            assertNull(reading.getEndString());
        }

        @Test
        void isFinishedReturnsEndNull() {
            Reading reading = new Reading(BOOK, BEGINNING, null, PAGES_PER_HOUR);
            assertFalse(reading.isFinished());
            reading.setEnd(END);
            assertTrue(reading.isFinished());
        }
    }

    @Nested
    class SetterTests {
        Reading reading;

        @BeforeEach
        void setup() {
            reading = new Reading(BOOK, BEGINNING, END, PAGES_PER_HOUR);
        }

        @Test
        void setBeginningAssertsAfterEnd() {
            try {
                reading.setBeginning(END.plusDays(1));
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            assertEquals(BEGINNING, reading.getBeginning());
        }

        @Test
        void setBeginningAllowsEnd() {
            try {
                reading.setBeginning(END);
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        void setEndAllowsNull() {
            try {
                reading.setEnd(null);
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
            assertNull(reading.getEnd());
        }

        @Test
        void setEndAssertsBeforeBeginning() {
            try {
                reading.setEnd(BEGINNING.minusDays(1));
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            assertEquals(END, reading.getEnd());
        }

        @Test
        void setEndAllowsBeginning() {
            try {
                reading.setEnd(BEGINNING);
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        void setPagesPerHourAssertsZero() {
            try {
                reading.setPagesPerHour(0);
                fail();
            } catch (IllegalArgumentException ignored) {
            }
        }

        @Test
        void setPagesPerHourAssertsNegative() {
            try {
                reading.setPagesPerHour(-1);
                fail();
            } catch (IllegalArgumentException ignored) {
            }
        }
    }
}
