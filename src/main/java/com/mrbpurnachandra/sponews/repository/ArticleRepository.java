package com.mrbpurnachandra.sponews.repository;

import com.mrbpurnachandra.sponews.model.Article;
import com.mrbpurnachandra.sponews.model.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends CrudRepository<Article, Long>, PagingAndSortingRepository<Article, Long> {
    Iterable<Article> findTop10ByOrderByPublishedOnDesc();

    Iterable<Article> findTop10ByTitleLikeIgnoreCase(String title);

    Page<Article> findArticlesByAuthorOrderByPublishedOnDesc(Author author, Pageable pageable);
}
