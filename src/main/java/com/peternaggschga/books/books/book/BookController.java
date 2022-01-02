package com.peternaggschga.books.books.book;

import com.peternaggschga.books.author.Author;
import com.peternaggschga.books.author.AuthorManagement;
import com.peternaggschga.books.books.BookManagement;
import com.peternaggschga.books.books.series.Series;
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
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A controller dealing with requests concerning {@link Book}s.
 */
@Controller
public class BookController {
    private static final Logger LOG = LoggerFactory.getLogger(BookController.class);
    @NotNull
    private final BookManagement bookManagement;
    @NotNull
    private final AuthorManagement authorManagement;

    /**
     * Creates a new {@link BookController} instance with the given {@link BookManagement} and {@link AuthorManagement}.
     *
     * @param bookManagement   must not be null.
     * @param authorManagement must not be null.
     */
    public BookController(@NonNull BookManagement bookManagement, @NonNull AuthorManagement authorManagement) {
        this.bookManagement = bookManagement;
        this.authorManagement = authorManagement;
    }

    @GetMapping("/books")
    public String showBooks(Model model) {
        model.addAttribute("authorExists", authorManagement.getAuthorCount() > 0);
        model.addAttribute("books", bookManagement.findAllBooks());
        return "books/book/books";
    }

    @GetMapping("/books/add")
    public String addBook(Model model, @SuppressWarnings("unused") EditBookForm form) {
        model.addAttribute("languages", BookManagement.LANGUAGES);
        model.addAttribute("authors", authorManagement.findAllAuthors());
        model.addAttribute("seriesIterable", bookManagement.findAllSeries());
        model.addAttribute("id", -1);
        return "books/book/edit_book";
    }

    @GetMapping("/books/{id}")
    public String editBook(@PathVariable long id, Model model, EditBookForm form) {
        Book book = bookManagement.findBookById(id);
        model.addAttribute("languages", BookManagement.LANGUAGES);
        model.addAttribute("authors", authorManagement.findAllAuthors());
        model.addAttribute("seriesIterable", bookManagement.findAllSeries());
        model.addAttribute("id", book.getId());
        form.setTitle(book.getTitle());
        form.setAuthors(book.getAuthors().stream().map(Author::getId).collect(Collectors.toList()));
        form.setPublishedString(book.getPublished().toString());
        form.setIsbn(book.getIsbn());
        form.setPages(book.getPages());
        form.setLanguageString(book.getLanguage().toString());
        form.setSeries(bookManagement.findSeriesByBook(book).map(Series::getId).toList());
        return "books/book/edit_book";
    }

    @PostMapping("/books/save")
    public String saveBook(long id, Model model, @Valid EditBookForm form, Errors result) {
        if (result.hasErrors()) {
            LOG.warn("Fehlerhafte Formulardaten: " + result.getAllErrors());
            model.addAttribute("languages", BookManagement.LANGUAGES);
            model.addAttribute("authors", authorManagement.findAllAuthors());
            model.addAttribute("seriesIterable", bookManagement.findAllSeries());
            model.addAttribute("id", id);
            return "books/book/edit_book";
        }
        Set<Author> authors = form.getAuthors().stream().map(authorManagement::findAuthorById)
                .collect(Collectors.toSet());
        Book book;
        if (id < 0) {
            book = bookManagement.createBook(form, authors);
        } else {
            book = bookManagement.updateBook(id, form, authors);
        }
        bookManagement.removeBookFromAllSeries(book);
        for (long seriesId : form.getSeries()) {
            bookManagement.addBooksToSeries(book, seriesId);
        }
        return "redirect:/books";
    }

    @PostMapping("/books/delete")
    public String deleteBook(long id) {
        bookManagement.deleteBook(id);
        return "redirect:/books";
    }
}
