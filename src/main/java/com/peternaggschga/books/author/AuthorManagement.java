package com.peternaggschga.books.author;

import com.neovisionaries.i18n.CountryCode;
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
    private final AuthorRepository repository;

    /**
     * Creates a new {@link AuthorManagement} instance with the given repository.
     *
     * @param repository must not be null.
     */
    public AuthorManagement(@NotNull AuthorRepository repository) {
        this.repository = repository;
    }

    /**
     * Creates a new {@link Author} instance with the given firstName, lastName, birthDate, deathDate and nationality.
     * The new instance is saved into the repository.
     *
     * @param firstName   must not be null or blank.
     * @param lastName    must not be null or blank.
     * @param birthDate   must not be null.
     * @param deathDate   can be null, if {@link Author} is currently alive.
     * @param nationality must not be null.
     * @return the new {@link Author} instance.
     */
    public Author createAuthor(@NotNull @NotBlank String firstName, @NotNull @NotBlank String lastName,
                               @NotNull LocalDate birthDate, LocalDate deathDate, @NotNull CountryCode nationality) {
        return repository.save(new Author(firstName, lastName, birthDate, deathDate, nationality));
    }

    /**
     * Creates a new {@link Author} instance with the given form. The new instance is saved into the repository. Wrapper
     * function of {@link AuthorManagement#createAuthor(String, String, LocalDate, LocalDate, CountryCode)}.
     *
     * @param form must be valid, must not be null.
     * @return the new {@link Author} instance.
     * @see AuthorManagement#createAuthor(String, String, LocalDate, LocalDate, CountryCode)
     */
    public Author createAuthor(@NotNull @Valid CreateAuthorForm form) {
        return createAuthor(form.getFirstName(), form.getLastName(), form.getBirthDate(), form.getDeathDate(),
                form.getCountryCode());
    }

    /**
     * Returns all {@link Author}s present in repository.
     *
     * @return an {@link Iterable} containing all {@link Author} instances in repository.
     */
    public Iterable<Author> findAll() {
        return repository.findAll();
    }
}
