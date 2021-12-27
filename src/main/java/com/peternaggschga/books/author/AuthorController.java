package com.peternaggschga.books.author;

import com.neovisionaries.i18n.CountryCode;
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
 * A controller dealing with requests concerning {@link Author}s.
 */
@Controller
public class AuthorController {
    private static final Logger LOG = LoggerFactory.getLogger(AuthorController.class);
    @NotNull
    private final AuthorManagement management;

    /**
     * Creates a new {@link AuthorController} instance with the given {@link AuthorManagement}.
     *
     * @param management must not be null.
     */
    public AuthorController(@NonNull AuthorManagement management) {
        this.management = management;
    }

    @GetMapping("/authors")
    public String showAuthors(Model model) {
        model.addAttribute("authors", management.findAllAuthors());
        return "author/authors";
    }

    @GetMapping("/authors/add")
    public String addAuthor(Model model, CreateAuthorForm form) {
        form.setCountryCodeString(CountryCode.US.toString());
        model.addAttribute("countries", CountryCode.values());
        model.addAttribute("id", -1);
        return "author/edit_author";
    }

    @GetMapping("/authors/{id}")
    public String editAuthor(@PathVariable long id, Model model, CreateAuthorForm form) {
        Author author = management.findAuthorById(id);
        model.addAttribute("countries", CountryCode.values());
        model.addAttribute("id", author.getId());
        form.setFirstName(author.getFirstName());
        form.setLastName(author.getLastName());
        if (author.getBirthDate() != null) {
            form.setBirthDateString(author.getBirthDate().toString());
        }
        if (author.getDeathDate() != null) {
            form.setDeathDateString(author.getDeathDate().toString());
        }
        form.setCountryCodeString(author.getNationality().toString());
        return "author/edit_author";
    }

    @PostMapping("/authors/save")
    public String saveAuthor(long id, Model model, @Valid CreateAuthorForm form, Errors result) {
        if (result.hasErrors()) {
            LOG.warn("Fehlerhafte Formulardaten: " + result.getAllErrors());
            model.addAttribute("countries", CountryCode.values());
            model.addAttribute("id", id);
            return "author/edit_author";
        }
        if (id < 0) {
            management.createAuthor(form);
        } else {
            management.updateAuthor(id, form);
        }
        return "redirect:/authors";
    }

    @PostMapping("/authors/delete")
    public String deleteAuthor(long id) {
        management.deleteAuthor(id);
        return "redirect:/authors";
    }
}
