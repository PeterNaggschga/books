package com.peternaggschga.books.author;

import com.neovisionaries.i18n.CountryCode;
import com.peternaggschga.books.books.BookManagement;
import com.peternaggschga.books.books.book.Book;
import lombok.NonNull;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Service managing access to the {@link AuthorRepository} and {@link Author} instances.
 */
@Service
@Transactional
public class AuthorManagement {
    @NotNull
    private final AuthorRepository authorRepository;
    @NotNull
    private final BookManagement bookManagement;

    /**
     * Creates a new {@link AuthorManagement} instance with the given {@link AuthorRepository} and
     * {@link BookManagement}.
     *
     * @param authorRepository must not be null.
     * @param bookManagement   must not be null.
     */
    public AuthorManagement(@NonNull AuthorRepository authorRepository, @NonNull BookManagement bookManagement) {
        this.authorRepository = authorRepository;
        this.bookManagement = bookManagement;
    }

    /**
     * Creates a new {@link Author} instance with the given firstName, lastName, birthDate, deathDate and nationality.
     * The new instance is saved into the {@link AuthorRepository}.
     *
     * @param firstName   must not be null or blank.
     * @param lastName    must not be null or blank.
     * @param birthDate   can be null, if unknown.
     * @param deathDate   can be null, if unknown.
     * @param nationality must not be null.
     * @return the new {@link Author} instance.
     */
    public Author createAuthor(@NonNull @NotBlank String firstName, @NonNull @NotBlank String lastName,
                               LocalDate birthDate, LocalDate deathDate, @NonNull CountryCode nationality) {
        return authorRepository.save(new Author(firstName, lastName, birthDate, deathDate, nationality));
    }

    /**
     * Creates a new {@link Author} instance with the given form. The new instance is saved into the
     * {@link AuthorRepository}.
     * Wrapper function of {@link AuthorManagement#createAuthor(String, String, LocalDate, LocalDate, CountryCode)}.
     *
     * @param form must be valid, must not be null.
     * @return the new {@link Author} instance.
     * @see AuthorManagement#createAuthor(String, String, LocalDate, LocalDate, CountryCode)
     */
    public Author createAuthor(@NonNull @Valid EditAuthorForm form) {
        return createAuthor(form.getFirstName(), form.getLastName(), form.getBirthDate(), form.getDeathDate(),
                form.getCountryCode());
    }

    /**
     * Updates the {@link Author} referred to by the given id with the given firstName, lastName, birthDate, deathDate
     * and nationality. The updated instance is saved to the {@link AuthorRepository}.
     *
     * @param id          must be valid.
     * @param firstName   must not be null or blank.
     * @param lastName    must not be null or blank.
     * @param birthDate   can be null, if unknown.
     * @param deathDate   can be null, if unknown.
     * @param nationality must not be null.
     * @return the updated {@link Author} instance.
     */
    public Author updateAuthor(long id, @NonNull @NotBlank String firstName, @NonNull @NotBlank String lastName,
                               LocalDate birthDate, LocalDate deathDate, @NonNull CountryCode nationality) {
        Author author = findAuthorById(id);
        author.setFirstName(firstName);
        author.setLastName(lastName);
        author.setBirthDate(birthDate);
        author.setDeathDate(deathDate);
        author.setNationality(nationality);
        return authorRepository.save(author);
    }

    /**
     * Updates the {@link Author} referred to by the given id with the given {@link EditAuthorForm}. Wrapper function
     * of {@link AuthorManagement#updateAuthor(long, String, String, LocalDate, LocalDate, CountryCode)}.
     *
     * @param id   must be valid.
     * @param form must be valid, must not be null.
     * @return the updated {@link Author} instance.
     * @see AuthorManagement#updateAuthor(long, String, String, LocalDate, LocalDate, CountryCode)
     */
    public Author updateAuthor(long id, @NonNull @Valid EditAuthorForm form) {
        return updateAuthor(id, form.getFirstName(), form.getLastName(), form.getBirthDate(), form.getDeathDate(),
                form.getCountryCode());
    }

    /**
     * Deletes the given {@link Author} from {@link AuthorRepository}.
     *
     * @param author must not be null.
     */
    public void deleteAuthor(@NonNull Author author) {
        for (Book book : bookManagement.findBooksByAuthor(author)) {
            if (book.getAuthors().size() == 1) {
                bookManagement.deleteBook(book);
            }
        }
        authorRepository.delete(author);
    }

    /**
     * Deletes the {@link Author} referenced by the given id from {@link AuthorRepository}.
     * Wrapper function of {@link AuthorManagement#deleteAuthor(Author)}.
     *
     * @param id must be valid.
     * @see AuthorManagement#deleteAuthor(Author)
     */
    public void deleteAuthor(long id) {
        deleteAuthor(findAuthorById(id));
    }

    /**
     * Returns all {@link Author}s present in {@link AuthorRepository}.
     *
     * @return an {@link Streamable} containing all {@link Author} instances in {@link AuthorRepository}.
     */
    public Streamable<Author> findAllAuthors() {
        return authorRepository.findAll();
    }

    /**
     * Returns the number of {@link Author}s saved in {@link AuthorRepository}.
     *
     * @return a long counting the number of {@link Book}s.
     */
    public long getAuthorCount() {
        return authorRepository.count();
    }

    /**
     * Returns the {@link Author} referenced by the given id.
     * If the id does not exist, a {@link java.util.NoSuchElementException} is thrown.
     *
     * @param id must be valid, else {@link java.util.NoSuchElementException} is thrown.
     * @return the {@link Author} referenced by id.
     */
    public Author findAuthorById(long id) {
        return authorRepository.findById(id).orElseThrow();
    }
}
