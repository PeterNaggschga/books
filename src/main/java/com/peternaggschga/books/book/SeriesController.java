package com.peternaggschga.books.book;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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
    public SeriesController(@NotNull BookManagement management) {
        this.management = management;
    }

    @GetMapping("/series")
    public String showSeries(Model model) {
        model.addAttribute("seriesIterable", management.findAllSeries());
        return "series";
    }

    @GetMapping("/series/add")
    public String addSeries(Model model) {
        model.addAttribute("books", management.findAllBooks());
        return "new_series";
    }

    @PostMapping("/series/add")
    public String addAuthor(Model model, @Valid CreateSeriesForm form, Errors result) {
        if (result.hasErrors()) {
            LOG.warn("Fehlerhafte Formulardaten: " + result.getAllErrors());
            model.addAttribute("books", management.findAllBooks());
            return "new_series";
        }
        management.createSeries(form);
        return "redirect:/series";
    }
}
