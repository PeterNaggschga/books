package com.peternaggschga.books.books.series;

import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * A container class for validation of form inputs concerning {@link Series}.
 */
public class EditSeriesForm {
    @NotNull
    @NotBlank
    @SuppressWarnings("FieldMayBeFinal")
    private String title;
    @SuppressWarnings("FieldMayBeFinal")
    private List<Long> books;

    public EditSeriesForm(String title, List<Long> books) {
        this.title = title;
        this.books = books;
    }

    public String getTitle() {
        return title;
    }

    public List<Long> getBooks() {
        return books;
    }

    public void setTitle(@NonNull @NotBlank String title) {
        this.title = title;
    }

    public void setBooks(@NonNull List<Long> books) {
        this.books = books;
    }
}
