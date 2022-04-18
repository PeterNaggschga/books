package com.peternaggschga.books.author;

import com.neovisionaries.i18n.CountryCode;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

/**
 * A container class for validation of form inputs concerning {@link Author}s.
 */
public class EditAuthorForm {
    @NotNull
    @NotBlank
    private String firstName;
    @NotNull
    @NotBlank
    private String lastName;
    @Pattern(regexp = "([0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])|)")
    private String birthDateString;
    @Pattern(regexp = "([0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])|)")
    private String deathDateString;
    @NotNull
    @Length(min = 2, max = 2)
    private String countryCodeString;

    public EditAuthorForm(String firstName, String lastName, String birthDateString, String deathDateString,
                          String countryCodeString) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDateString = birthDateString;
        this.deathDateString = deathDateString;
        this.countryCodeString = countryCodeString;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBirthDateString() {
        return birthDateString;
    }

    public String getDeathDateString() {
        return deathDateString;
    }

    public String getCountryCodeString() {
        return countryCodeString;
    }

    public LocalDate getBirthDate() {
        return birthDateString.isBlank() ? null : LocalDate.parse(birthDateString);
    }

    public LocalDate getDeathDate() {
        return deathDateString.isBlank() ? null : LocalDate.parse(deathDateString);
    }

    public CountryCode getCountryCode() {
        return CountryCode.getByCode(countryCodeString);
    }

    public void setFirstName(@NonNull @NotBlank String firstName) {
        if (firstName.isBlank()) {
            throw new IllegalArgumentException("Names must not be blank");
        }
        this.firstName = firstName;
    }

    public void setLastName(@NonNull @NotBlank String lastName) {
        if (lastName.isBlank()) {
            throw new IllegalArgumentException("Names must not be blank");
        }
        this.lastName = lastName;
    }

    public void setBirthDateString(
            @NonNull @Pattern(regexp = "[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])") String birthDateString) {
        if (!(birthDateString.isBlank() ||
                birthDateString.matches("[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])"))) {
            throw new IllegalArgumentException("String must match date format");
        }
        this.birthDateString = birthDateString;
    }

    public void setDeathDateString(
            @NonNull @Pattern(regexp = "[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])") String deathDateString) {
        if (!(deathDateString.isBlank() ||
                deathDateString.matches("[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])"))) {
            throw new IllegalArgumentException("String must match date format");
        }
        this.deathDateString = deathDateString;
    }

    public void setCountryCodeString(@NonNull @NotBlank String countryCodeString) {
        if (countryCodeString.isBlank()) {
            throw new IllegalArgumentException("CountryCodeString must not be blank");
        }
        this.countryCodeString = countryCodeString;
    }
}
