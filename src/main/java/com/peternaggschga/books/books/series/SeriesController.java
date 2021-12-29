package com.peternaggschga.books.books.series;

import com.peternaggschga.books.books.BookManagement;
import com.peternaggschga.books.books.book.Book;
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
import java.util.stream.Collectors;

/**
 * A controller dealing with requests concerning {@link Book}s.
 */
@Controller
public class SeriesController {
    private static final Logger LOG = LoggerFactory.getLogger(SeriesController.class);
    @NotNull
    private final BookManagement management;

    /**
     * Creates a new {@link SeriesController} instance with the given {@link BookManagement}.
     *
     * @param management must not be null.
     */
    public SeriesController(@NonNull BookManagement management) {
        this.management = management;
    }

    @GetMapping("/series")
    public String showSeries(Model model) {
        model.addAttribute("seriesIterable", management.findAllSeries());
        return "books/series/series";
    }

    @GetMapping("/series/add")
    public String addSeries(Model model, @SuppressWarnings("unused") EditSeriesForm form) {
        model.addAttribute("books", management.findAllBooks());
        model.addAttribute("id", -1);
        return "books/series/edit_series";
    }

    @GetMapping("/series/{id}")
    public String editAuthor(@PathVariable long id, Model model, EditSeriesForm form) {
        Series series = management.findSeriesById(id);
        model.addAttribute("books", management.findAllBooks());
        model.addAttribute("id", series.getId());
        form.setTitle(series.getTitle());
        form.setBooks(series.getBooks().stream().map(Book::getId).collect(Collectors.toList()));
        return "books/series/edit_series";
    }

    @PostMapping("/series/save")
    public String saveSeries(long id, Model model, @Valid EditSeriesForm form, Errors result) {
        if (result.hasErrors()) {
            LOG.warn("Fehlerhafte Formulardaten: " + result.getAllErrors());
            model.addAttribute("books", management.findAllBooks());
            model.addAttribute("id", id);
            return "books/series/edit_series";
        }
        if (id < 0) {
            management.createSeries(form);
        } else {
            management.updateSeries(id, form);
        }
        return "redirect:/series";
    }

    @PostMapping("/series/delete")
    public String deleteSeries(long id) {
        management.deleteSeries(id);
        return "redirect:/series";
    }
}
