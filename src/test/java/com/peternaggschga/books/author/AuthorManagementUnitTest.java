package com.peternaggschga.books.author;

import com.neovisionaries.i18n.CountryCode;
import com.peternaggschga.books.books.BookManagement;
import com.peternaggschga.books.books.book.Book;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Streamable;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthorManagementUnitTest {

    @BeforeAll
    static void canBeInstantiated() {
        try {
            new AuthorManagement(mock(AuthorRepository.class), mock(BookManagement.class));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Nested
    class CreateAuthorTests {
        Set<Author> authors = new HashSet<>();
        AuthorManagement management;

        @BeforeEach
        void setup() {
            authors.clear();
            AuthorRepository repository = mock(AuthorRepository.class);
            when(repository.save(any())).thenAnswer(invocationOnMock -> {
                authors.add(invocationOnMock.getArgument(0));
                return invocationOnMock.getArgument(0);
            });
            management = new AuthorManagement(repository, mock(BookManagement.class));
        }

        @Test
        void createAuthorAssertsFirstNameBlank() {
            EditAuthorForm form;
            try {
                management.createAuthor("", "Name", null, null, CountryCode.US);
                fail();
            } catch (IllegalArgumentException ignored) {
                assertEquals(0, authors.size());
            }
            form = new EditAuthorForm("", "Name", "", "", CountryCode.US.toString());
            try {
                management.createAuthor(form);
                fail();
            } catch (IllegalArgumentException ignored) {
                assertEquals(0, authors.size());
            }
            try {
                management.createAuthor(" ", "Name", null, null, CountryCode.US);
                fail();
            } catch (IllegalArgumentException ignored) {
                assertEquals(0, authors.size());
            }
            form = new EditAuthorForm(" ", "Name", "", "", CountryCode.US.toString());
            try {
                management.createAuthor(form);
                fail();
            } catch (IllegalArgumentException ignored) {
                assertEquals(0, authors.size());
            }
        }

        @Test
        void createAuthorAssertsLastNameBlank() {
            EditAuthorForm form;
            try {
                management.createAuthor("Name", "", null, null, CountryCode.US);
                fail();
            } catch (IllegalArgumentException ignored) {
                assertEquals(0, authors.size());
            }
            form = new EditAuthorForm("Name", "", "", "", CountryCode.US.toString());
            try {
                management.createAuthor(form);
                fail();
            } catch (IllegalArgumentException ignored) {
                assertEquals(0, authors.size());
            }
            try {
                management.createAuthor("Name", " ", null, null, CountryCode.US);
                fail();
            } catch (IllegalArgumentException ignored) {
                assertEquals(0, authors.size());
            }
            form = new EditAuthorForm("Name", " ", "", "", CountryCode.US.toString());
            try {
                management.createAuthor(form);
                fail();
            } catch (IllegalArgumentException ignored) {
                assertEquals(0, authors.size());
            }
        }

        @Test
        void createAuthorSavesInstanceWhenValid() {
            EditAuthorForm form;
            Author author = new Author("Vorname", "Nachname", null, null, CountryCode.US);
            try {
                Author savedAuthor = management.createAuthor(author.getFirstName(), author.getLastName(),
                        author.getBirthDate(), author.getDeathDate(), author.getNationality());
                assertEquals(1, authors.size());
                assertTrue(authors.contains(savedAuthor));
                assertEquals(author.getFirstName(), savedAuthor.getFirstName());
                assertEquals(author.getLastName(), savedAuthor.getLastName());
                assertEquals(author.getBirthDate(), savedAuthor.getBirthDate());
                assertEquals(author.getDeathDate(), savedAuthor.getDeathDate());
                assertEquals(author.getNationality(), savedAuthor.getNationality());
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
            authors.clear();
            form = new EditAuthorForm("Vorname", "Nachname", "", "", CountryCode.US.toString());
            try {
                Author savedAuthor = management.createAuthor(form);
                assertEquals(1, authors.size());
                assertTrue(authors.contains(savedAuthor));
                assertEquals(form.getFirstName(), savedAuthor.getFirstName());
                assertEquals(form.getLastName(), savedAuthor.getLastName());
                assertEquals(form.getBirthDate(), savedAuthor.getBirthDate());
                assertEquals(form.getDeathDate(), savedAuthor.getDeathDate());
                assertEquals(form.getCountryCode(), savedAuthor.getNationality());
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        void createAuthorAssertsInvalidForm() {
            EditAuthorForm form;
            form = new EditAuthorForm("", "Autorisch", "2002-04-02", LocalDate.now().toString(), CountryCode.US.toString());
            try {
                management.createAuthor(form);
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            form = new EditAuthorForm(null, "Autorisch", "2002-04-02", LocalDate.now().toString(), CountryCode.US.toString());
            try {
                management.createAuthor(form);
                fail();
            } catch (NullPointerException ignored) {
            }
            form = new EditAuthorForm("Autor", "", "2002-04-02", LocalDate.now().toString(), CountryCode.US.toString());
            try {
                management.createAuthor(form);
                fail();
            } catch (IllegalArgumentException ignored) {
            }
            form = new EditAuthorForm("Autor", null, "2002-04-02", LocalDate.now().toString(), CountryCode.US.toString());
            try {
                management.createAuthor(form);
                fail();
            } catch (NullPointerException ignored) {
            }
            form = new EditAuthorForm("Autor", "Autorisch", null, LocalDate.now().toString(), CountryCode.US.toString());
            try {
                management.createAuthor(form);
                fail();
            } catch (NullPointerException ignored) {
            }
            form = new EditAuthorForm("Autor", "Autorisch", "2002-04-02", null, CountryCode.US.toString());
            try {
                management.createAuthor(form);
                fail();
            } catch (NullPointerException ignored) {
            }
            form = new EditAuthorForm("Autor", "Autorisch", "2002-04-02", LocalDate.now().toString(), "");
            try {
                management.createAuthor(form);
                fail();
            } catch (NullPointerException ignored) {
            }
            form = new EditAuthorForm("Autor", "Autorisch", "2002-04-02", LocalDate.now().toString(), null);
            try {
                management.createAuthor(form);
                fail();
            } catch (NullPointerException ignored) {
            }
        }
    }

    @Nested
    @SpringBootTest
    class UpdateAuthorTests {
        long numberOfTestAuthors = 3;
        @Autowired
        AuthorRepository repository;
        AuthorManagement management;
        Set<Author> testAuthors = new HashSet<>();

        @BeforeEach
        void setup() {
            BookManagement bookManagement = mock(BookManagement.class);
            when(bookManagement.findBooksByAuthor(any())).thenReturn(Streamable.empty());
            management = new AuthorManagement(repository, bookManagement);
            for (long i = 1; i <= numberOfTestAuthors; i++) {
                testAuthors.add(management.createAuthor("Vorname" + i, "Nachname", null, null, CountryCode.US));
            }
        }

        @AfterEach
        void tearDown() {
            for (Author author : testAuthors) {
                management.deleteAuthor(author);
            }
            testAuthors.clear();
        }

        @Test
        void updateAuthorUpdatesAttributes() {
            for (Author author : testAuthors) {
                Author updatedAuthor = management.updateAuthor(author.getId(), author.getLastName(),
                        author.getFirstName(), LocalDate.now(), LocalDate.now(), CountryCode.UK);
                assertEquals(author.getId(), updatedAuthor.getId());
                assertEquals(author.getLastName(), updatedAuthor.getFirstName());
                assertEquals(author.getFirstName(), updatedAuthor.getLastName());
                assertEquals(LocalDate.now(), updatedAuthor.getBirthDate());
                assertEquals(LocalDate.now(), updatedAuthor.getDeathDate());
                assertEquals(CountryCode.UK, updatedAuthor.getNationality());
            }
        }

        @Test
        void updateAuthorFormUpdatesAttributes() {
            for (Author author : testAuthors) {
                EditAuthorForm form = new EditAuthorForm(author.getLastName(), author.getFirstName(),
                        LocalDate.now().toString(), LocalDate.now().toString(), CountryCode.UK.toString());
                Author updatedAuthor = management.updateAuthor(author.getId(), form);
                assertEquals(author.getId(), updatedAuthor.getId());
                assertEquals(author.getLastName(), updatedAuthor.getFirstName());
                assertEquals(author.getFirstName(), updatedAuthor.getLastName());
                assertEquals(LocalDate.now(), updatedAuthor.getBirthDate());
                assertEquals(LocalDate.now(), updatedAuthor.getDeathDate());
                assertEquals(CountryCode.UK, updatedAuthor.getNationality());
            }
        }

        @Test
        void updateAuthorSavesUpdatedInstance() {
            for (Author author : testAuthors) {
                management.updateAuthor(author.getId(), author.getFirstName(), "Anders", null, null,
                        author.getNationality());
                Author updated = management.findAuthorById(author.getId());
                assertNotEquals(author, updated);
                EditAuthorForm form = new EditAuthorForm(author.getFirstName(), "Ganzanders", "", "",
                        author.getNationality().toString());
                management.updateAuthor(author.getId(), form);
                assertNotEquals(updated, management.findAuthorById(updated.getId()));
            }
        }

        @Test
        void updateAuthorAssertsInvalidForm() {
            Author author = testAuthors.stream().findAny().orElseThrow();
            EditAuthorForm form;
            form = new EditAuthorForm(null, author.getLastName(), "", "", author.getNationality().toString());
            try {
                management.updateAuthor(author.getId(), form);
                fail();
            } catch (NullPointerException ignored) {
                assertEquals(author.getFirstName(), management.findAuthorById(author.getId()).getFirstName());
            }
            form = new EditAuthorForm("", author.getLastName(), "", "", author.getNationality().toString());
            try {
                management.updateAuthor(author.getId(), form);
                fail();
            } catch (IllegalArgumentException ignored) {
                assertEquals(author.getFirstName(), management.findAuthorById(author.getId()).getFirstName());
            }
            form = new EditAuthorForm(author.getFirstName(), null, "", "", author.getNationality().toString());
            try {
                management.updateAuthor(author.getId(), form);
                fail();
            } catch (NullPointerException ignored) {
                assertEquals(author.getLastName(), management.findAuthorById(author.getId()).getLastName());
            }
            form = new EditAuthorForm(author.getFirstName(), "", "", "", author.getNationality().toString());
            try {
                management.updateAuthor(author.getId(), form);
                fail();
            } catch (IllegalArgumentException ignored) {
                assertEquals(author.getLastName(), management.findAuthorById(author.getId()).getLastName());
            }
            form = new EditAuthorForm(author.getFirstName(), author.getLastName(), null, "", author.getNationality().toString());
            try {
                management.updateAuthor(author.getId(), form);
                fail();
            } catch (NullPointerException ignored) {
                assertEquals(author.getBirthDate(), management.findAuthorById(author.getId()).getBirthDate());
            }
            form = new EditAuthorForm(author.getFirstName(), author.getLastName(), "", null, author.getNationality().toString());
            try {
                management.updateAuthor(author.getId(), form);
                fail();
            } catch (NullPointerException ignored) {
                assertEquals(author.getDeathDate(), management.findAuthorById(author.getId()).getDeathDate());
            }
            form = new EditAuthorForm(author.getFirstName(), author.getLastName(), "", "", null);
            try {
                management.updateAuthor(author.getId(), form);
                fail();
            } catch (NullPointerException ignored) {
                assertEquals(author.getNationality(), management.findAuthorById(author.getId()).getNationality());
            }
        }

        @Test
        void updateAuthorAssertsInvalidId() {
            try {
                management.updateAuthor(0, "Name", "Name", null, null, CountryCode.US);
                fail();
            } catch (NoSuchElementException ignored) {
            }
        }

        @Test
        void updateAuthorAssertsFirstNameBlank() {
            Author author = testAuthors.stream().findAny().orElseThrow();
            try {
                management.updateAuthor(author.getId(), "", "Name", null, null, author.getNationality());
                fail();
            } catch (IllegalArgumentException ignored) {
                assertEquals(author.getFirstName(), management.findAuthorById(author.getId()).getFirstName());
            }
        }

        @Test
        void updateAuthorAssertsLastNameBlank() {
            Author author = testAuthors.stream().findAny().orElseThrow();
            try {
                management.updateAuthor(author.getId(), "Name", "", null, null, author.getNationality());
                fail();
            } catch (IllegalArgumentException ignored) {
                assertEquals(author.getLastName(), management.findAuthorById(author.getId()).getLastName());
            }
        }

        @Test
        void updateAuthorAssertsBirthAfterDeath() {
            Author author = testAuthors.stream().findAny().orElseThrow();
            try {
                management.updateAuthor(author.getId(), "Name", "Name", LocalDate.now(), LocalDate.now().minusDays(1),
                        author.getNationality());
                fail();
            } catch (IllegalArgumentException ignored) {
                assertEquals(author.getBirthDate(), management.findAuthorById(author.getId()).getBirthDate());
                assertEquals(author.getDeathDate(), management.findAuthorById(author.getId()).getDeathDate());
            }
        }
    }

    @Nested
    @SpringBootTest
    class DeleteAuthorTests {
        final int numberOfTestAuthors = 3;
        final int numberOfTestBooks = 2;
        @Autowired
        AuthorManagement authorManagement;
        @Autowired
        BookManagement bookManagement;
        Set<Author> authors = new HashSet<>();
        Set<Book> books = new HashSet<>();

        @BeforeEach
        void setup() {
            for (int i = 0; i < numberOfTestAuthors; i++) {
                Author author = authorManagement.createAuthor("Autor", String.valueOf(i), null, null, CountryCode.US);
                authors.add(author);
                for (int j = 0; j < numberOfTestBooks; j++) {
                    books.add(bookManagement.createBook("Buch", List.of(author), LocalDate.now(), "978-3-492-70479-3",
                            10, Locale.GERMAN));
                }
            }
        }

        @AfterEach
        void tearDown() {
            for (Book book : books) {
                bookManagement.deleteBook(book);
            }
            for (Author author : authors) {
                authorManagement.deleteAuthor(author);
            }
        }

        @Test
        void deleteAuthorDeletesAuthorById() {
            for (Author author : authors) {
                authorManagement.deleteAuthor(author.getId());
                try {
                    authorManagement.findAuthorById(author.getId());
                    fail();
                } catch (NoSuchElementException ignored) {
                }
            }
        }

        @Test
        void deleteAuthorDeletesAuthorByInstance() {
            for (Author author : authors) {
                authorManagement.deleteAuthor(author);
                try {
                    authorManagement.findAuthorById(author.getId());
                    fail();
                } catch (NoSuchElementException ignored) {
                }
            }
        }

        @Test
        void deleteAuthorDeletesAssociatedBooks() {
            for (Author author : authors) {
                long bookCount = bookManagement.getBookCount();
                authorManagement.deleteAuthor(author);
                assertEquals(bookCount - numberOfTestBooks, bookManagement.getBookCount());
            }
        }

        @Test
        void deleteAuthorAssertsInvalidId() {
            try {
                authorManagement.deleteAuthor(0);
                fail();
            } catch (NoSuchElementException ignored) {
            }
        }
    }
}
