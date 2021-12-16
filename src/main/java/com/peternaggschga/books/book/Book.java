package com.peternaggschga.books.book;

import com.peternaggschga.books.author.Author;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

/**
 * An Entity representing a Book.
 */
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
    private Set<Author> authors;
    @NotNull
    private LocalDate published;
    @NotNull
    @Pattern(regexp = ISBN_REGEX)
    private String isbn;
    @Positive
    private int pages;
    @NotNull
    private Locale language;

    /**
     * No-arg constructor of {@link Book}, only used by {@link org.springframework.boot.SpringApplication Spring}.
     */
    protected Book() {
    }

    /**
     * Creates a new {@link Book} instance with the given title, authors, published, isbn, pages and language.
     *
     * @param title     must not be null or blank.
     * @param authors   must not be null or empty.
     * @param published must not be null.
     * @param isbn      must not be null, must match {@link Book#ISBN_REGEX}.
     * @param pages     must be positive.
     * @param language  must not be null.
     * @see Book#Book(String, Author, LocalDate, String, int, Locale)
     */
    public Book(@NotNull @NotBlank String title, @NotNull @NotEmpty Collection<Author> authors,
                @NotNull LocalDate published, @NotNull String isbn, @Positive int pages, @NotNull Locale language) {
        setTitle(title);
        setAuthors(authors);
        setPublished(published);
        setIsbn(isbn);
        setPages(pages);
        setLanguage(language);
    }

    /**
     * Creates a new {@link Book} instance with the given title, author, published, isbn, pages and language.
     * Wrapper of {@link Book#Book(String, Collection, LocalDate, String, int, Locale)}.
     *
     * @param title     must not be null or blank.
     * @param author    must not be null.
     * @param published must not be null.
     * @param isbn      must not be null, must match {@link Book#ISBN_REGEX}.
     * @param pages     must be positive.
     * @param language  must not be null.
     * @see Book#Book(String, Collection, LocalDate, String, int, Locale)
     */
    public Book(@NotNull @NotBlank String title, @NotNull Author author, @NotNull LocalDate published,
                @NotNull String isbn, @Positive int pages, @NotNull Locale language) {
        this(title, Set.of(author), published, isbn, pages, language);
    }

    public long getId() {
        return id;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(@NotNull Author author) {
        this.authors = Set.of(Objects.requireNonNull(author, "Author must not be null"));
    }

    public void setAuthors(@NotNull @NotEmpty Collection<Author> authors) {
        if (Objects.requireNonNull(authors, "List of authors must not be null").isEmpty()) {
            throw new IllegalArgumentException("List of authors must not be empty");
        }
        this.authors = new HashSet<>(authors);
    }

    /**
     * Returns a {@link String} containing the names of all {@link Author}s of this book.
     *
     * @return a {@link String} of names, never null.
     */
    public String getAuthorString() {
        List<Author> authors = new ArrayList<>(this.authors);
        StringBuilder builder = new StringBuilder(authors.get(0).toString());
        for (int i = 1; i < authors.size(); i++) {
            builder.append(", ").append(authors.get(i).toString());
        }
        return builder.toString();
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

    public int getPages() {
        return pages;
    }

    public Locale getLanguage() {
        return language;
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

    public LocalDate getPublished() {
        return published;
    }

    /**
     * Returns a locally formatted {@link String} representing the date of publication.
     *
     * @return a {@link String} representing published.
     */
    public String getPublishedString() {
        return published.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
    }

    @Override
    public String toString() {
        return getAuthorString() + ": " + title;
    }
}
