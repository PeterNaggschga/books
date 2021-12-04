package com.peternaggschga.books.author;

import com.neovisionaries.i18n.CountryCode;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * An entity representing a person writing {@link com.peternaggschga.books.book.Book Book}s.
 */
@Entity
public class Author {
    @Id
    @GeneratedValue
    private long id;
    @NotNull
    @NotBlank
    private String firstName;
    @NotNull
    @NotBlank
    private String lastName;
    @NotNull
    private LocalDate birthDate;
    private LocalDate deathDate;
    @NotNull
    private CountryCode nationality;

    /**
     * No-arg constructor of {@link Author}, only used by {@link org.springframework.boot.SpringApplication Spring}.
     */
    protected Author() {
    }

    /**
     * Creates a new {@link Author} instance with the given firstName, lastName, birthDate, deathDate and nationality.
     *
     * @param firstName   must not be null or blank.
     * @param lastName    must not be null or blank.
     * @param birthDate   must not be null.
     * @param deathDate   can be null, if {@link Author} is currently alive.
     * @param nationality must not be null.
     */
    public Author(@NotNull @NotBlank String firstName, @NotNull @NotBlank String lastName, @NotNull LocalDate birthDate,
                  LocalDate deathDate, @NotNull CountryCode nationality) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.deathDate = deathDate;
        this.nationality = nationality;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public LocalDate getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(LocalDate deathDate) {
        this.deathDate = deathDate;
    }

    public CountryCode getNationality() {
        return nationality;
    }
}
