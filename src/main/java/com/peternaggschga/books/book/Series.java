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
    private SortedSet<Book> books;

    /**
     * No-arg constructor of {@link Series}, only used by {@link org.springframework.boot.SpringApplication Spring}.
     */
    protected Series() {
    }

    /**
     * Creates a new {@link Series} instance with the given title and books.
     *
     * @param title must not be null or blank.
     * @param books must not be null.
     */
    public Series(@NotNull @NotBlank String title, Collection<Book> books) {
        this.books = new TreeSet<>(Comparator.comparing(Book::getPublished));
        setTitle(title);
        addAll(books);
    }

    /**
     * Adds all {@link Book}s of the given {@link Collection} and their associated {@link Author}s to the series.
     *
     * @param books can be null.
     * @return true if books were changed by the operation.
     */
    @SuppressWarnings("UnusedReturnValue")
    public boolean addAll(Collection<Book> books) {
        return books != null && this.books.addAll(books);
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
        this.title = title.trim();
    }

    /**
     * Returns a {@link SortedSet} of {@link Author}s who wrote a {@link Book} in this series.
     * The authors are sorted by the amount of books contained in the series they worked on.
     *
     * @return a {@link SortedSet} of {@link Author}s.
     */
    public SortedSet<Author> getAuthors() {
        SortedSet<Author> result = new TreeSet<>((author, t1) ->
                (int) (this.books.stream().filter(book -> book.getAuthors().contains(t1)).count()
                        - this.books.stream().filter(book -> book.getAuthors().contains(author)).count()));
        result.addAll(books.stream().flatMap((Function<Book, Stream<Author>>) book ->
                book.getAuthors().stream()).collect(Collectors.toSet()));
        return result;
    }

    public SortedSet<Book> getBooks() {
        return books;
    }

    /**
     * Removes the given {@link Book} from books.
     *
     * @param book can be null.
     * @return true if books were changed by the operation.
     */
    @SuppressWarnings("UnusedReturnValue")
    public boolean remove(Book book) {
        return book != null && books.remove(book);
    }

    public void clear() {
        books.clear();
    }
}
