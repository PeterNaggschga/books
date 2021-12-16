package com.peternaggschga.books.book;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

/**
 * A container class for validation of form inputs concerning {@link Book}s.
 */
public class CreateBookForm {
    @NotNull
    @NotBlank
    @SuppressWarnings("FieldMayBeFinal")
    private String title;
    @NotNull
    @NotBlank
    @SuppressWarnings("FieldMayBeFinal")
    private String publishedString;
    @NotNull
    @Pattern(regexp = Book.ISBN_REGEX)
    @SuppressWarnings("FieldMayBeFinal")
    private String isbn;
    @NotNull
    @Positive
    @SuppressWarnings("FieldMayBeFinal")
    private Integer pages;
    @NotNull
    @NotBlank
    @SuppressWarnings("FieldMayBeFinal")
    private String languageString;
    @NotNull
    @NotEmpty
    @SuppressWarnings("FieldMayBeFinal")
    private List<Long> authors;
    @SuppressWarnings("FieldMayBeFinal")
    private List<Long> series;

    public CreateBookForm(String title, List<Long> authors, String publishedString, String isbn, Integer pages,
                          String languageString, List<Long> series) {
        this.title = title;
        this.authors = authors;
        this.publishedString = publishedString;
        this.isbn = isbn;
        this.pages = pages;
        this.languageString = languageString;
        this.series = series;
    }

    public String getTitle() {
        return title;
    }

    public List<Long> getAuthors() {
        return authors;
    }

    @SuppressWarnings("unused")
    public String getPublishedString() {
        return publishedString;
    }

    public LocalDate getPublished() {
        return publishedString == null ? null : LocalDate.parse(publishedString);
    }

    public String getIsbn() {
        return isbn;
    }

    public Integer getPages() {
        return pages;
    }

    @SuppressWarnings("unused")
    public String getLanguageString() {
        return languageString;
    }

    public Locale getLanguage() {
        return BookManagement.LANGUAGES[0].toString().equals(languageString) ? BookManagement.LANGUAGES[0]
                : BookManagement.LANGUAGES[1];
    }

    public List<Long> getSeries() {
        return series;
    }
}
