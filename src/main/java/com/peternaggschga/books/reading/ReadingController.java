package com.peternaggschga.books.reading;

import com.peternaggschga.books.books.BookManagement;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * A controller dealing with requests concerning {@link Reading}s.
 */
@Controller
public class ReadingController {
    private static final Logger LOG = LoggerFactory.getLogger(ReadingController.class);
    @NotNull
    private final ReadingManagement readingManagement;
    @NotNull
    private final BookManagement bookManagement;

    /**
     * Creates a new {@link ReadingController} instance with the given {@link ReadingManagement} and
     * {@link BookManagement}.
     *
     * @param readingManagement must not be null.
     * @param bookManagement    must not be null.
     */
    public ReadingController(@NonNull ReadingManagement readingManagement, @NonNull BookManagement bookManagement) {
        this.readingManagement = readingManagement;
        this.bookManagement = bookManagement;
    }

    @GetMapping("/readings")
    public String showReadings(Model model) {
        model.addAttribute("bookExists", bookManagement.getBookCount() > 0);
        model.addAttribute("readings", readingManagement.findAllReadings());
        return "reading/readings";
    }

    @GetMapping("/readings/add")
    public String addReading(Model model, @SuppressWarnings("unused") EditReadingForm form) {
        model.addAttribute("books", bookManagement.findAllBooks());
        model.addAttribute("id", -1);
        return "reading/edit_reading";
    }

    @GetMapping("/readings/{id}")
    public String editAuthor(@PathVariable long id, Model model, EditReadingForm form) {
        Reading reading = readingManagement.findReadingById(id);
        model.addAttribute("books", bookManagement.findAllBooks());
        model.addAttribute("id", id);
        form.setBookId(id);
        form.setBeginningString(reading.getBeginning().toString());
        if (form.getEnd() != null) {
            form.setEndString(form.getEnd().toString());
        }
        form.setPagesPerHour(reading.getPagesPerHour());
        return "reading/edit_reading";
    }

    @PostMapping("/readings/save")
    public String saveReading(long id, Model model, @Valid EditReadingForm form, Errors result) {
        if (result.hasErrors()) {
            LOG.warn("Fehlerhafte Formulardaten: " + result.getAllErrors());
            model.addAttribute("books", bookManagement.findAllBooks());
            model.addAttribute("id", id);
            return "reading/edit_reading";
        }
        if (form.getEnd() != null && form.getBeginning().isAfter(form.getEnd())) {
            LOG.warn("Fehlerhafte Formulardaten: Beginn nach Ende!");
            result.rejectValue("endString", "IsBeforeBeginningDate");
            model.addAttribute("books", bookManagement.findAllBooks());
            model.addAttribute("id", id);
            return "reading/edit_reading";
        }
        if (id < 0) {
            readingManagement.createReading(form, bookManagement.findBookById(form.getBookId()));
        } else {
            readingManagement.updateReading(id, form, bookManagement.findBookById(form.getBookId()));
        }
        return "redirect:/readings";
    }

    @PostMapping("/readings/delete")
    public String deleteReading(long id) {
        readingManagement.deleteReading(id);
        return "redirect:/readings";
    }
}
