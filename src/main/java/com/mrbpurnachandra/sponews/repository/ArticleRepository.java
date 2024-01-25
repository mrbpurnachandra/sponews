package com.mrbpurnachandra.sponews.repository;

import com.mrbpurnachandra.sponews.model.Article;
import com.mrbpurnachandra.sponews.model.Author;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends CrudRepository<Article, Long> {
    Iterable<Article> findTop10ByOrderByPublishedOnDesc();
    Iterable<Article> findTop10ArticlesByAuthorOrderByPublishedOnDesc(Author author);

    @Query("select a from Article a where upper(a.title) like upper(?1) ")
    Iterable<Article> findByTitleLikeIgnoreCase(String title);
}
