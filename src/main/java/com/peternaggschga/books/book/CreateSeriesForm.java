package com.peternaggschga.books.book;

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
    private List<String> books;

    public CreateSeriesForm(String title, List<String> books) {
        this.title = title;
        this.books = books;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getBooks() {
        return books;
    }
}
