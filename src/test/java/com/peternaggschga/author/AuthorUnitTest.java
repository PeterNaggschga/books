package com.peternaggschga.author;

import com.neovisionaries.i18n.CountryCode;
import com.peternaggschga.books.author.Author;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Author.class)
public class AuthorUnitTest {
    static final String FIRST_NAME = "John Ronald Reuel";
    static final String LAST_NAME = "Tolkien";
    static final LocalDate BIRTH_DATE = LocalDate.of(1892, 10, 10);
    static final LocalDate DEATH_DATE = LocalDate.of(1973, 10, 10);
    static final CountryCode NATIONALITY = CountryCode.UK;

    @BeforeAll
    static void canBeInstantiated() {
        try {
            new Author(FIRST_NAME, LAST_NAME, BIRTH_DATE, DEATH_DATE, NATIONALITY);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Nested
    class ConstructorTests {

        @Test
        void constructorAssertsFirstNameNull() {
            try {
                new Author(null, LAST_NAME, BIRTH_DATE, DEATH_DATE, NATIONALITY);
                fail();
            } catch (NullPointerException ignored) {
            }
        }

        @Test
        void constructorAssertsFirstNameEmpty() {
            try {
                new Author(" ", LAST_NAME, BIRTH_DATE, DEATH_DATE, NATIONALITY);
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            try {
                new Author("", LAST_NAME, BIRTH_DATE, DEATH_DATE, NATIONALITY);
                fail();
            } catch (IllegalArgumentException ignored) {
            }
        }

        @Test
        void constructorTrimsStrings() {
            Author author = new Author(" " + FIRST_NAME + " ", " " + LAST_NAME + " ", BIRTH_DATE, DEATH_DATE, NATIONALITY);
            assertEquals(FIRST_NAME, author.getFirstName());
            assertEquals(LAST_NAME, author.getLastName());
        }

        @Test
        void constructorAssertsLastNameNull() {
            try {
                new Author(FIRST_NAME, null, BIRTH_DATE, DEATH_DATE, NATIONALITY);
                fail();
            } catch (NullPointerException ignored) {
            }
        }

        @Test
        void constructorAssertsLastNameEmpty() {
            try {
                new Author(FIRST_NAME, " ", BIRTH_DATE, DEATH_DATE, NATIONALITY);
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            try {
                new Author(FIRST_NAME, "", BIRTH_DATE, DEATH_DATE, NATIONALITY);
                fail();
            } catch (IllegalArgumentException ignored) {
            }
        }

        @Test
        void constructorAllowsBirthDateNull() {
            try {
                new Author(FIRST_NAME, LAST_NAME, null, DEATH_DATE, NATIONALITY);
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        void constructorAssertsBirthDateFuture() {
            try {
                new Author(FIRST_NAME, LAST_NAME, LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), NATIONALITY);
                fail();
            } catch (IllegalArgumentException ignored) {
            }
        }

        @Test
        void constructorAllowsDeathDateNull() {
            try {
                new Author(FIRST_NAME, LAST_NAME, BIRTH_DATE, null, NATIONALITY);
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        void constructorAssertsDeathDateFuture() {
            try {
                new Author(FIRST_NAME, LAST_NAME, BIRTH_DATE, LocalDate.now().plusDays(1), NATIONALITY);
                fail();
            } catch (IllegalArgumentException ignored) {
            }
        }

        @Test
        void constructorAssertsBirthDateAfterDeathDate() {
            try {
                new Author(FIRST_NAME, LAST_NAME, DEATH_DATE, BIRTH_DATE, NATIONALITY);
                fail();
            } catch (IllegalArgumentException ignored) {
            }
        }

        @Test
        void constructorAssertsNationalityNull() {
            try {
                new Author(FIRST_NAME, LAST_NAME, BIRTH_DATE, DEATH_DATE, null);
                fail();
            } catch (NullPointerException ignored) {
            }
        }
    }

    @Nested
    class getterTests {

        @Test
        void getDateStringReturnsNull() {
            Author author = new Author(FIRST_NAME, LAST_NAME, null, null, NATIONALITY);
            assertNull(author.getDeathDateString());
            assertNull(author.getDeathDateString());
        }

        @Test
        void getDateReturnsFormattedStrings() {
            Author author = new Author(FIRST_NAME, LAST_NAME, BIRTH_DATE, DEATH_DATE, NATIONALITY);
            assertEquals(BIRTH_DATE.getDayOfMonth() + "." + BIRTH_DATE.getMonthValue() + "." + BIRTH_DATE.getYear(),
                    author.getBirthDateString());
            assertEquals(DEATH_DATE.getDayOfMonth() + "." + DEATH_DATE.getMonthValue() + "." + DEATH_DATE.getYear(),
                    author.getDeathDateString());
        }
    }

    @Nested
    class SetterTests {
        Author author;

        @BeforeEach
        void setup() {
            author = new Author(FIRST_NAME, LAST_NAME, BIRTH_DATE, DEATH_DATE, NATIONALITY);
        }

        @Test
        void setFirstNameAssertsNull() {
            try {
                author.setFirstName(null);
                fail();
            } catch (NullPointerException ignored) {
            }
            assertEquals(FIRST_NAME, author.getFirstName());
        }

        @Test
        void setFirstNameAssertsEmpty() {
            try {
                author.setFirstName(" ");
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            try {
                author.setFirstName("");
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            assertEquals(FIRST_NAME, author.getFirstName());
        }

        @Test
        void setNameTrimsStrings() {
            author.setFirstName(" " + FIRST_NAME + " ");
            assertEquals(FIRST_NAME, author.getFirstName());
            author.setLastName(" " + LAST_NAME + " ");
            assertEquals(LAST_NAME, author.getLastName());
        }

        @Test
        void setLastNameAssertsNull() {
            try {
                author.setLastName(null);
                fail();
            } catch (NullPointerException ignored) {
            }
            assertEquals(LAST_NAME, author.getLastName());
        }

        @Test
        void setLastNameAssertsEmpty() {
            try {
                author.setLastName(" ");
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            try {
                author.setLastName("");
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            assertEquals(LAST_NAME, author.getLastName());
        }

        @Test
        void setBirthDateAllowsNull() {
            try {
                author.setBirthDate(null);
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
            assertNull(author.getBirthDate());
        }

        @Test
        void setBirthDateAssertsFuture() {
            try {
                author.setBirthDate(LocalDate.now().plusDays(1));
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            assertEquals(BIRTH_DATE, author.getBirthDate());
        }

        @Test
        void setBirthDateAssertsAfterDeathDate() {
            try {
                author.setBirthDate(DEATH_DATE.plusDays(1));
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            assertEquals(BIRTH_DATE, author.getBirthDate());
        }

        @Test
        void setDeathDateAllowsNull() {
            try {
                author.setDeathDate(null);
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
            assertNull(author.getDeathDate());
        }

        @Test
        void setDeathDateAssertsFuture() {
            try {
                author.setDeathDate(LocalDate.now().plusDays(1));
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            assertEquals(DEATH_DATE, author.getDeathDate());
        }

        @Test
        void setDeathDateAssertsBeforeBirthDate() {
            try {
                author.setDeathDate(BIRTH_DATE.minusDays(1));
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            assertEquals(DEATH_DATE, author.getDeathDate());
        }

        @Test
        void constructorAssertsNationalityNull() {
            try {
                new Author(FIRST_NAME, LAST_NAME, BIRTH_DATE, DEATH_DATE, null);
                fail();
            } catch (NullPointerException ignored) {
            }
            assertEquals(NATIONALITY, author.getNationality());
        }
    }
}
