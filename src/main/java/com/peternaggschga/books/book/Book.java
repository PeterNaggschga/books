package com.peternaggschga.books.book;

import com.peternaggschga.books.author.Author;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Entity
public class Book {
    public static final String ISBN_REGEX = "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$";
    @GeneratedValue
    @Id
    private long id;
    @NotNull
    @NotBlank
    private String title;
    @NotNull
    @NotEmpty
    @ManyToMany
    private List<Author> authors;
    @NotNull
    private LocalDate published;
    @NotNull
    @Pattern(regexp = ISBN_REGEX)
    private String isbn;
    @Positive
    private int pages;
    @NotNull
    private Locale language;

    protected Book() {
    }

    public Book(@NotNull @NotBlank String title, @NotNull @NotEmpty List<Author> authors, @NotNull LocalDate published,
                @NotNull String isbn, @Positive int pages, @NotNull Locale language) {
        setTitle(title);
        setAuthors(authors);
        setPublished(published);
        setIsbn(isbn);
        setPages(pages);
        setLanguage(language);
    }

    public Book(@NotNull @NotBlank String title, @NotNull Author author, @NotNull LocalDate published,
                @NotNull String isbn, @Positive int pages, @NotNull Locale language) {
        setTitle(title);
        setAuthors(author);
        setPublished(published);
        setIsbn(isbn);
        setPages(pages);
        setLanguage(language);
    }

    public void setAuthors(@NotNull Author author) {
        this.authors = List.of(Objects.requireNonNull(author, "Author must not be null"));
    }

    public void setAuthors(@NotNull @NotEmpty List<Author> authors) {
        if (Objects.requireNonNull(authors, "List of authors must not be null").isEmpty()) {
            throw new IllegalArgumentException("List of authors must not be empty");
        }
        this.authors = authors;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(@NotNull @NotBlank String title) {
        if (Objects.requireNonNull(title, "Title must not be null").isBlank()) {
            throw new IllegalArgumentException("Title must not be blank");
        }
        this.title = title.trim();
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(@NotNull String isbn) {
        if (!Objects.requireNonNull(isbn).trim().matches(ISBN_REGEX)) {
            throw new IllegalArgumentException("ISBN must match regex " + ISBN_REGEX);
        }
        this.isbn = isbn.trim();
    }

    public void setPublished(@NotNull LocalDate published) {
        this.published = Objects.requireNonNull(published, "Date of publication must not be null");
    }

    public void setPages(@Positive int pages) {
        if (pages <= 0) {
            throw new IllegalArgumentException("Number of pages must be positive");
        }
        this.pages = pages;
    }

    public void setLanguage(@NotNull Locale language) {
        this.language = Objects.requireNonNull(language, "Language must not be null");
    }
}
