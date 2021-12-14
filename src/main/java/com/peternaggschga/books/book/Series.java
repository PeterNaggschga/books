package com.peternaggschga.books.book;

import com.peternaggschga.books.author.Author;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * An entity representing a series of {@link Book}s.
 */
@Entity
public class Series {
    @GeneratedValue
    @Id
    private long id;
    @NotNull
    @NotBlank
    private String title;
    @NotNull
    @ManyToMany
    private SortedSet<Author> authors;
    @NotNull
    @ManyToMany
    private SortedSet<Book> books;

    protected Series() {
    }

    /**
     * Creates a new {@link Series} instance with the given title and books.
     *
     * @param title must not be null or blank.
     * @param books must not be null.
     */
    public Series(@NotNull @NotBlank String title, @NotNull Collection<Book> books) {
        this.books = new TreeSet<>(Comparator.comparing(Book::getPublished));
        this.authors = new TreeSet<>((author, t1) -> (int) (this.books.stream().filter(book -> book.getAuthors().contains(t1)).count() - this.books.stream().filter(book -> book.getAuthors().contains(author)).count()));
        setTitle(title);
        addAll(books);
    }

    /**
     * Adds all {@link Book}s of the given {@link Collection} and their associated {@link Author}s to the series.
     *
     * @param books must not be null.
     * @return true if books or authors were changed by the operation.
     */
    @SuppressWarnings("UnusedReturnValue")
    public boolean addAll(@NotNull Collection<Book> books) {
        return this.books.addAll(Objects.requireNonNull(books, "Collection of books must not be null")) ||
                this.authors.addAll(books.stream().flatMap((Function<Book, Stream<Author>>) book -> book.getAuthors().stream()).collect(Collectors.toSet()));
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(@NotNull @NotBlank String title) {
        if (Objects.requireNonNull(title, "Title must not be null").isBlank()) {
            throw new IllegalArgumentException("Title must not be blank");
        }
        this.title = title;
    }

    public SortedSet<Author> getAuthors() {
        return authors;
    }

    public SortedSet<Book> getBooks() {
        return books;
    }
}
