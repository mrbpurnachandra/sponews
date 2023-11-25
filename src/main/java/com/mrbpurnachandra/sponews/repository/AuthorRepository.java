package com.mrbpurnachandra.sponews.repository;

import com.mrbpurnachandra.sponews.model.Author;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends CrudRepository<Author, Integer> {
    public Optional<Author> findByEmail(String email);
}
