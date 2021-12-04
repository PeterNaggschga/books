package com.peternaggschga.books.author;

import com.neovisionaries.i18n.CountryCode;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Service
@Transactional
public class AuthorManagement {
    @NotNull
    private final AuthorRepository repository;

    public AuthorManagement(@NotNull AuthorRepository repository) {
        this.repository = repository;
    }

    public Author createAuthor(@NotNull @NotBlank String firstName, @NotNull @NotBlank String lastName,
                               @NotNull LocalDate birthDate, LocalDate deathDate, @NotNull CountryCode nationality) {
        return repository.save(new Author(firstName, lastName, birthDate, deathDate, nationality));
    }

    Iterable<Author> findAll() {
        return repository.findAll();
    }
}
