package com.peternaggschga.books.reading;

import com.peternaggschga.books.books.book.Book;
import com.peternaggschga.books.books.series.Series;
import com.peternaggschga.books.books.series.SeriesRepository;
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

    /**
     * Creates a new {@link ReadingManagement} instance with the given {@link ReadingRepository}.
     *
     * @param readingRepository must not be null.
     */
    public ReadingManagement(@NonNull ReadingRepository readingRepository) {
        this.readingRepository = readingRepository;
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
    public Reading createReading(@NonNull @Valid CreateReadingForm form, @NonNull Book book) {
        return createReading(book, form.getBeginning(), form.getEnd(), form.getPagesPerHour());
    }

    /**
     * Deletes the given {@link Reading} from {@link ReadingRepository}.
     *
     * @param reading must not be null.
     */
    public void deleteReading(@NonNull Reading reading) {
        readingRepository.delete(reading);
    }

    /**
     * Deletes the {@link Reading} referenced by the given id from {@link ReadingRepository}.
     *
     * @param id must be valid.
     */
    public void deleteReading(long id) {
        readingRepository.deleteById(id);
    }

    /**
     * Deletes all {@link Reading}s concerning the given {@link Book} from {@link ReadingRepository}.
     *
     * @param book must be valid.
     */
    public void deleteReadingsByBook(@NonNull Book book) {
        for (Reading reading : findReadingsByBook(book)) {
            deleteReading(reading);
        }
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

    /**
     * Returns all {@link Series} saved in {@link SeriesRepository} that contain the given {@link Book}.
     *
     * @param book must not be null.
     * @return a {@link Streamable} containing {@link Series}.
     */
    public Streamable<Reading> findReadingsByBook(@NonNull Book book) {
        return readingRepository.findReadingsByBook(book);
    }
}
