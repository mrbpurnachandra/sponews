package com.mrbpurnachandra.sponews.service;

import com.mrbpurnachandra.sponews.model.Author;
import com.mrbpurnachandra.sponews.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Optional<Author> findAuthorByEmail(String email) {
            return authorRepository.findByEmail(email);
    }

    public boolean isAuthorRegistered(String authorEmail) {
        Optional<Author> optionalAuthor = findAuthorByEmail(authorEmail);
        return optionalAuthor.isPresent();
    }

    public Author save(Author author) {
        return authorRepository.save(author);
    }

    public Optional<Author> findById(Integer authorId) {
        return authorRepository.findById(authorId);
    }
}
