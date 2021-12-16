package com.peternaggschga.books.reading;

import com.peternaggschga.books.book.Book;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Objects;

/**
 * An entity representing one reading of a certain {@link Book}.
 */
@Entity
public class Reading {
    @GeneratedValue
    @Id
    private long id;
    @NotNull
    @ManyToOne
    private Book book;
    @NotNull
    private LocalDate beginning;
    private LocalDate end;
    @Positive
    private int pagesPerHour;

    /**
     * No-arg constructor of {@link Reading}, only used by {@link org.springframework.boot.SpringApplication Spring}.
     */
    protected Reading() {
    }

    /**
     * Creates a new {@link Reading} with the given {@link Book}, beginning and end.
     *
     * @param book         must not be null.
     * @param beginning    must not be null.
     * @param end          can be null (if reading is not finished yet).
     * @param pagesPerHour must be positive.
     */
    public Reading(@NotNull Book book, @NotNull LocalDate beginning, LocalDate end, @Positive int pagesPerHour) {
        setBook(book);
        setBeginning(beginning);
        setEnd(end);
        setPagesPerHour(pagesPerHour);
    }

    public long getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(@NotNull Book book) {
        this.book = Objects.requireNonNull(book, "Book must not be null");
    }

    public LocalDate getBeginning() {
        return beginning;
    }

    public void setBeginning(@NotNull LocalDate beginning) {
        if (end != null
                && Objects.requireNonNull(beginning, "Date of beginning must not be null").isAfter(end)) {
            throw new IllegalArgumentException("Beginning must be before end");
        }
        this.beginning = Objects.requireNonNull(beginning, "Date of beginning must not be null");
    }

    /**
     * Returns a locally formatted {@link String} representing the beginning of the reading.
     *
     * @return a {@link String} representing beginning.
     */
    public String getBeginningString() {
        return beginning.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        if (end != null && end.isBefore(beginning)) {
            throw new IllegalArgumentException("End must be after beginning");
        }
        this.end = end;
    }

    /**
     * Returns a locally formatted {@link String} representing the end of the reading.
     *
     * @return a {@link String} representing end.
     */
    public String getEndString() {
        return end == null ? null : end.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
    }

    public int getPagesPerHour() {
        return pagesPerHour;
    }

    public void setPagesPerHour(@Positive int pagesPerHour) {
        if (pagesPerHour <= 0) {
            throw new IllegalArgumentException("PagesPerHour must be positive");
        }
        this.pagesPerHour = pagesPerHour;
    }

    public boolean isFinished() {
        return end != null;
    }
}
