package com.peternaggschga.books.reading;

import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

/**
 * A container class for validation of form inputs concerning {@link Reading}s.
 */
public class EditReadingForm {
    @NotNull
    private Long bookId;
    @NotNull
    @NotBlank
    @Pattern(regexp = "[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])")
    private String beginningString;
    @NotNull
    @Pattern(regexp = "([0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])|)")
    private String endString;
    @NotNull
    @Positive
    private Integer pagesPerHour;

    public EditReadingForm(Long bookId, String beginningString, String endString, Integer pagesPerHour) {
        this.bookId = bookId;
        this.beginningString = beginningString;
        this.endString = endString;
        this.pagesPerHour = pagesPerHour;
    }

    public LocalDate getBeginning() {
        return LocalDate.parse(beginningString);
    }

    public LocalDate getEnd() {
        return endString.isBlank() ? null : LocalDate.parse(endString);
    }

    public Long getBookId() {
        return bookId;
    }

    public String getBeginningString() {
        return beginningString;
    }

    public String getEndString() {
        return endString;
    }

    public Integer getPagesPerHour() {
        return pagesPerHour;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public void setBeginningString(
            @NonNull @Pattern(regexp = "[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])") String beginningString) {
        if (!beginningString.matches("[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])")) {
            throw new IllegalArgumentException("String must match date format");
        }
        this.beginningString = beginningString;
    }

    public void setEndString(
            @NonNull @Pattern(regexp = "[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])") String endString) {
        if (!endString.matches("[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])")) {
            throw new IllegalArgumentException("String must match date format");
        }
        this.endString = endString;
    }

    public void setPagesPerHour(@Positive int pagesPerHour) {
        if (pagesPerHour <= 0) {
            throw new IllegalArgumentException("PagesPerHour must be positive");
        }
        this.pagesPerHour = pagesPerHour;
    }
}
