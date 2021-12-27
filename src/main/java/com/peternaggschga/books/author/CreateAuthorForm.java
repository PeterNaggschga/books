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
public class CreateAuthorForm {
    @NotNull
    @NotBlank
    @SuppressWarnings("FieldMayBeFinal")
    private String firstName;
    @NotNull
    @NotBlank
    @SuppressWarnings("FieldMayBeFinal")
    private String lastName;
    @NotNull
    @Pattern(regexp = "([0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])|)")
    @SuppressWarnings("FieldMayBeFinal")
    private String birthDateString;
    @NotNull
    @Pattern(regexp = "([0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])|)")
    @SuppressWarnings("FieldMayBeFinal")
    private String deathDateString;
    @NotNull
    @Length(min = 2, max = 2)
    private String countryCodeString;

    public CreateAuthorForm(String firstName, String lastName, String birthDateString, String deathDateString,
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

    @SuppressWarnings("unused")
    public String getBirthDateString() {
        return birthDateString;
    }

    @SuppressWarnings("unused")
    public String getDeathDateString() {
        return deathDateString;
    }

    @SuppressWarnings("unused")
    public String getCountryCodeString() {
        return countryCodeString;
    }

    public LocalDate getBirthDate() {
        return birthDateString.isEmpty() ? null : LocalDate.parse(birthDateString);
    }

    public LocalDate getDeathDate() {
        return deathDateString.isEmpty() ? null : LocalDate.parse(deathDateString);
    }

    public CountryCode getCountryCode() {
        return CountryCode.getByCode(countryCodeString);
    }

    public void setFirstName(@NonNull @NotBlank String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(@NonNull @NotBlank String lastName) {
        this.lastName = lastName;
    }

    public void setBirthDateString(@NonNull @NotBlank String birthDateString) {
        this.birthDateString = birthDateString;
    }

    public void setDeathDateString(@NonNull @NotBlank String deathDateString) {
        this.deathDateString = deathDateString;
    }

    public void setCountryCodeString(@NonNull @NotBlank String countryCodeString) {
        this.countryCodeString = countryCodeString;
    }
}
