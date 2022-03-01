package com.peternaggschga.books.author;

import com.neovisionaries.i18n.CountryCode;
import com.peternaggschga.books.books.book.Book;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Objects;

/**
 * An entity representing a person writing {@link Book}s.
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
     * @param birthDate   can be null, if unknown.
     * @param deathDate   can be null, if unknown.
     * @param nationality must not be null.
     */
    public Author(@NonNull @NotBlank String firstName, @NonNull @NotBlank String lastName, LocalDate birthDate,
                  LocalDate deathDate, @NonNull CountryCode nationality) {
        setFirstName(firstName);
        setLastName(lastName);
        setBirthDate(birthDate);
        setDeathDate(deathDate);
        setNationality(nationality);
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NonNull @NotBlank String firstName) {
        if (firstName.isBlank()) {
            throw new IllegalArgumentException("Firstname must not be blank");
        }
        this.firstName = firstName.trim();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(@NonNull @NotBlank String lastName) {
        if (lastName.isBlank()) {
            throw new IllegalArgumentException("Lastname must not be blank");
        }
        this.lastName = lastName.trim();
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        if (birthDate != null && deathDate != null && birthDate.isAfter(deathDate)) {
            throw new IllegalArgumentException("Date of birth must not be after date of death");
        }
        if (birthDate != null && birthDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date of birth must not be after today");
        }
        this.birthDate = birthDate;
    }

    public LocalDate getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(LocalDate deathDate) {
        if (deathDate != null && birthDate != null && deathDate.isBefore(birthDate)) {
            throw new IllegalArgumentException("Date of death must not be before date of birth");
        }
        if (deathDate != null && deathDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date of death must not be after today");
        }
        this.deathDate = deathDate;
    }

    public CountryCode getNationality() {
        return nationality;
    }

    public void setNationality(@NonNull CountryCode nationality) {
        this.nationality = nationality;
    }

    /**
     * Returns locally formatted String representing the birthDate.
     *
     * @return locally formatted String representing a date, can be null.
     * @see DateTimeFormatter#ofLocalizedDate(FormatStyle)
     */
    public String getBirthDateString() {
        return birthDate == null ? null : getBirthDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
    }

    /**
     * Returns locally formatted String representing the deathDate.
     *
     * @return locally formatted String representing a date, can be null.
     * @see DateTimeFormatter#ofLocalizedDate(FormatStyle)
     */
    public String getDeathDateString() {
        return deathDate == null ? null : getDeathDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
    }

    @Override
    public String toString() {
        return getFirstName() + " " + getLastName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Author author = (Author) o;
        return id == author.id && firstName.equals(author.firstName) && lastName.equals(author.lastName) &&
                Objects.equals(birthDate, author.birthDate) && Objects.equals(deathDate, author.deathDate) &&
                nationality == author.nationality;
    }
}
