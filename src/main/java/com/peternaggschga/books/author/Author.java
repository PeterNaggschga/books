package com.peternaggschga.books.author;

import com.neovisionaries.i18n.CountryCode;

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

    public void setFirstName(@NotNull @NotBlank String firstName) {
        if (Objects.requireNonNull(firstName, "Firstname must not be null").isBlank()) {
            throw new IllegalArgumentException("Firstname must not be blank");
        }
        this.firstName = firstName.trim();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(@NotNull @NotBlank String lastName) {
        if (Objects.requireNonNull(lastName, "Lastname must not be null").isBlank()) {
            throw new IllegalArgumentException("Lastname must not be blank");
        }
        this.lastName = lastName.trim();
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(@NotNull LocalDate birthDate) {
        Objects.requireNonNull(birthDate, "Date of birth must not be null");
        if (deathDate != null && birthDate.isAfter(deathDate)) {
            throw new IllegalArgumentException("Date of birth must not be after date of death");
        }
        if (birthDate.isAfter(LocalDate.now())) {
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

    public void setNationality(@NotNull CountryCode nationality) {
        this.nationality = Objects.requireNonNull(nationality, "Nationality must not be null");
    }

    /**
     * Returns locally formatted String representing the birthDate.
     *
     * @return locally formatted String representing a date, never null.
     * @see DateTimeFormatter#ofLocalizedDate(FormatStyle)
     */
    public String getBirthDateString() {
        return getBirthDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
    }

    /**
     * Returns locally formatted String representing the deathDate.
     *
     * @return locally formatted String representing a date, can be null.
     * @see DateTimeFormatter#ofLocalizedDate(FormatStyle)
     */
    public String getDeathDateString() {
        if (deathDate == null) {
            return null;
        }
        return getDeathDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
    }

    @Override
    public String toString() {
        return getFirstName() + " " + getLastName();
    }
}
