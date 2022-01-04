package com.peternaggschga.books.books.book;

import com.peternaggschga.books.books.BookManagement;
import lombok.NonNull;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * A container class for validation of form inputs concerning {@link Book}s.
 */
public class EditBookForm {
    @NotNull
    @NotBlank
    private String title;
    @NotNull
    @NotBlank
    private String publishedString;
    @NotNull
    @Pattern(regexp = Book.ISBN_REGEX)
    private String isbn;
    @NotNull
    @Positive
    private Integer pages;
    @NotNull
    @NotBlank
    private String languageString;
    @NotNull
    @NotEmpty
    private List<Long> authors;
    private List<Long> series;

    public EditBookForm(String title, List<Long> authors, String publishedString, String isbn, Integer pages,
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

    public String getPublishedString() {
        return publishedString;
    }

    public LocalDate getPublished() {
        return LocalDate.parse(publishedString);
    }

    public String getIsbn() {
        return isbn;
    }

    public Integer getPages() {
        return pages;
    }

    public String getLanguageString() {
        return languageString;
    }

    public Locale getLanguage() {
        return BookManagement.LANGUAGES[0].toString().equals(languageString) ? BookManagement.LANGUAGES[0]
                : BookManagement.LANGUAGES[1];
    }

    public List<Long> getSeries() {
        return series == null ? new ArrayList<>() : series;
    }

    public void setTitle(@NonNull @NotBlank String title) {
        if (title.isBlank()) {
            throw new IllegalArgumentException("Title must not be blank");
        }
        this.title = title;
    }

    public void setPublishedString(
            @NonNull @Pattern(regexp = "[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])") String publishedString) {
        if (!publishedString.matches("[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])")) {
            throw new IllegalArgumentException("Date must match date format");
        }
        this.publishedString = publishedString;
    }

    public void setIsbn(@NonNull @Pattern(regexp = Book.ISBN_REGEX) String isbn) {
        if (!isbn.matches(Book.ISBN_REGEX)) {
            throw new IllegalArgumentException("Isbn must match regex " + Book.ISBN_REGEX);
        }
        this.isbn = isbn;
    }

    public void setPages(@NonNull @Positive Integer pages) {
        if (pages <= 0) {
            throw new IllegalArgumentException("Pages must be positive");
        }
        this.pages = pages;
    }

    public void setLanguageString(@NonNull @NotBlank String languageString) {
        if (Arrays.stream(BookManagement.LANGUAGES).map(Locale::toString).noneMatch(s -> s.equals(languageString))) {
            throw new IllegalArgumentException("LanguageString must be toString of Locale in "
                    + Arrays.toString(BookManagement.LANGUAGES));
        }
        this.languageString = languageString;
    }

    public void setAuthors(@NonNull @NotEmpty List<Long> authors) {
        if (authors.isEmpty()) {
            throw new IllegalArgumentException("Authors must not be empty");
        }
        this.authors = authors;
    }

    public void setSeries(@NonNull List<Long> series) {
        this.series = series;
    }
}
