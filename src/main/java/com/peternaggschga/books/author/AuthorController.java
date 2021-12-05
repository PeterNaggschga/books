package com.peternaggschga.books.author;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.validation.constraints.NotNull;

/**
 * A controller dealing with requests concerning {@link Author}s.
 */
@Controller
public class AuthorController {
    @NotNull
    private final AuthorManagement management;

    /**
     * Creates a new {@link AuthorController} instance with the given management.
     *
     * @param management must not be null.
     */
    public AuthorController(@NotNull AuthorManagement management) {
        this.management = management;
    }

    @GetMapping("/authors")
    public String showAuthors(Model model) {
        model.addAttribute("authors", management.findAll());
        return "authors";
    }
}
