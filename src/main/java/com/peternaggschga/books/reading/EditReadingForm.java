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
    @SuppressWarnings("FieldMayBeFinal")
    private Long bookId;
    @NotNull
    @NotBlank
    @Pattern(regexp = "[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])")
    @SuppressWarnings("FieldMayBeFinal")
    private String beginningString;
    @NotNull
    @Pattern(regexp = "([0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])|)")
    @SuppressWarnings("FieldMayBeFinal")
    private String endString;
    @NotNull
    @Positive
    @SuppressWarnings("FieldMayBeFinal")
    private Integer pagesPerHour;

    public EditReadingForm(Long bookId, String beginningString, String endString, Integer pagesPerHour) {
        this.bookId = bookId;
        this.beginningString = beginningString;
        this.endString = endString;
        this.pagesPerHour = pagesPerHour;
    }

    public LocalDate getBeginning() {
        return beginningString == null ? null : LocalDate.parse(beginningString);
    }

    public LocalDate getEnd() {
        return endString == null || endString.isBlank() ? null : LocalDate.parse(endString);
    }

    public Long getBookId() {
        return bookId;
    }

    @SuppressWarnings("unused")
    public String getBeginningString() {
        return beginningString;
    }

    @SuppressWarnings("unused")
    public String getEndString() {
        return endString;
    }

    public Integer getPagesPerHour() {
        return pagesPerHour;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public void setBeginningString(@NonNull @NotBlank String beginningString) {
        this.beginningString = beginningString;
    }

    public void setEndString(@NonNull @NotBlank String endString) {
        this.endString = endString;
    }

    public void setPagesPerHour(int pagesPerHour) {
        this.pagesPerHour = pagesPerHour;
    }
}
