package com.peternaggschga.books.reading;

import com.peternaggschga.books.books.BookManagement;
import com.peternaggschga.books.books.book.Book;
import lombok.NonNull;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

/**
 * Service managing access to the {@link ReadingRepository} and {@link Reading} instances.
 */
@Service
@Transactional
public class ReadingManagement {
    @NotNull
    private final ReadingRepository readingRepository;
    @NotNull
    private final BookManagement bookManagement;

    /**
     * Creates a new {@link ReadingManagement} instance with the given {@link ReadingRepository} and
     * {@link BookManagement}.
     *
     * @param readingRepository must not be null.
     * @param bookManagement    must not be null.
     */
    public ReadingManagement(@NonNull ReadingRepository readingRepository, @NonNull BookManagement bookManagement) {
        this.readingRepository = readingRepository;
        this.bookManagement = bookManagement;
    }

    /**
     * Creates a new {@link Reading} instance with the given {@link Book}, beginning, end and number of pages per hour.
     * Saves the new instance to the {@link ReadingRepository}.
     *
     * @param book         must not be null.
     * @param beginning    must not be null.
     * @param end          can be null (if reading is not finished yet).
     * @param pagesPerHour must be positive.
     * @return the new {@link Reading} instance.
     */
    public Reading createReading(@NonNull Book book, @NonNull LocalDate beginning, LocalDate end,
                                 @Positive int pagesPerHour) {
        return readingRepository.save(new Reading(book, beginning, end, pagesPerHour));
    }

    /**
     * Creates a new {@link Reading} instance with the given {@link CreateReadingForm}. Saves the new instance to the
     * {@link ReadingRepository}.
     * Wrapper function of {@link ReadingManagement#createReading(Book, LocalDate, LocalDate, int)}.
     *
     * @param form must be valid, must not be null.
     * @return the new {@link Reading} instance.
     * @see ReadingManagement#createReading(Book, LocalDate, LocalDate, int)
     */
    @SuppressWarnings("UnusedReturnValue")
    public Reading createReading(@NonNull @Valid CreateReadingForm form) {
        return createReading(bookManagement.findBookById(form.getBookId()), form.getBeginning(), form.getEnd(),
                form.getPagesPerHour());
    }

    /**
     * Returns all {@link Reading}s present in {@link ReadingRepository}.
     *
     * @return an {@link Iterable} containing all {@link Reading} instances in {@link ReadingRepository}.
     */
    public Streamable<Reading> findAllReadings() {
        return readingRepository.findAll();
    }

    /**
     * Returns the {@link Reading} referenced by the given id.
     * If the id does not exist, a {@link java.util.NoSuchElementException} is thrown.
     *
     * @param id must be valid, else {@link java.util.NoSuchElementException} is thrown.
     * @return the {@link Reading} referenced by id.
     */
    public Reading findReadingById(long id) {
        return readingRepository.findById(id).orElseThrow();
    }
}
