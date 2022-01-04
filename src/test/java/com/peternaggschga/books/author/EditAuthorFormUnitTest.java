package com.peternaggschga.books.author;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EditAuthorFormUnitTest {

    @BeforeAll
    static void canBeInstantiated() {
        try {
            new EditAuthorForm(null, null, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Nested
    class GetterTests {

        @Test
        void getBirthDateReturnsNull() {
            EditAuthorForm form = new EditAuthorForm(null, null, "", null, null);
            assertNull(form.getBirthDate());
        }

        @Test
        void getDeathDateReturnsNull() {
            EditAuthorForm form = new EditAuthorForm(null, null, null, "", null);
            assertNull(form.getDeathDate());
        }

        @Test
        void getCountryCodeInterpretsAllCountryCodes() {
            for (CountryCode code : CountryCode.values()) {
                EditAuthorForm form = new EditAuthorForm(null, null, null, null, code.toString());
                assertEquals(code, form.getCountryCode());
            }
        }
    }

    @Nested
    class SetterTests {
        EditAuthorForm form;

        @BeforeEach
        void setup() {
            form = new EditAuthorForm(null, null, null, null, null);
        }

        @Test
        void setFirstNameAssertsBlank() {
            try {
                form.setFirstName("");
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            assertNull(form.getFirstName());
            try {
                form.setFirstName(" ");
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            assertNull(form.getFirstName());
        }

        @Test
        void setLastNameAssertsBlank() {
            try {
                form.setLastName("");
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            assertNull(form.getLastName());
            try {
                form.setLastName(" ");
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            assertNull(form.getLastName());
        }

        @Test
        void setBirthDateStringAssertsBlank() {
            try {
                form.setBirthDateString("");
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            assertNull(form.getBirthDateString());
            try {
                form.setBirthDateString(" ");
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            assertNull(form.getBirthDateString());
        }

        @Test
        void setBirthDateStringAssertsFormat() {
            for (String date :
                    List.of("123-12-12", "2004-13-12", "2004-00-12", "2004-20-12", "2004-12-00", "2004-12-32")) {
                try {
                    form.setBirthDateString(date);
                    fail();
                } catch (IllegalArgumentException ignored) {
                }
                assertNull(form.getBirthDateString());
            }
        }

        @Test
        void setDeathDateStringAssertsBlank() {
            try {
                form.setDeathDateString("");
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            assertNull(form.getDeathDateString());
            try {
                form.setDeathDateString(" ");
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            assertNull(form.getDeathDateString());
        }

        @Test
        void setDeathDateStringAssertsFormat() {
            for (String date :
                    List.of("123-12-12", "2004-13-12", "2004-00-12", "2004-20-12", "2004-12-00", "2004-12-32")) {
                try {
                    form.setDeathDateString(date);
                    fail();
                } catch (IllegalArgumentException ignored) {
                }
                assertNull(form.getDeathDateString());
            }
        }

        @Test
        void setCountryCodeStringAssertsBlank() {
            try {
                form.setCountryCodeString("");
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            assertNull(form.getCountryCodeString());
            try {
                form.setCountryCodeString(" ");
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            assertNull(form.getCountryCodeString());
        }
    }
}
