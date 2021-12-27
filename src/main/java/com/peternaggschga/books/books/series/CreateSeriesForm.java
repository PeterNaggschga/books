package com.peternaggschga.books.books.series;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * A container class for validation of form inputs concerning {@link Series}.
 */
public class CreateSeriesForm {
    @NotNull
    @NotBlank
    @SuppressWarnings("FieldMayBeFinal")
    private String title;
    @SuppressWarnings("FieldMayBeFinal")
    private List<Long> books;

    public CreateSeriesForm(String title, List<Long> books) {
        this.title = title;
        this.books = books;
    }

    public String getTitle() {
        return title;
    }

    public List<Long> getBooks() {
        return books;
    }
}
