package com.mrbpurnachandra.sponews.repository;

import com.mrbpurnachandra.sponews.model.Article;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends CrudRepository<Article, Long> {
    Iterable<Article> findTop10ByOrderByPublishedOnDesc();
}
