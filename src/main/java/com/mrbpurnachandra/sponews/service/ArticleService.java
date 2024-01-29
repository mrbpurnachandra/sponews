package com.mrbpurnachandra.sponews.service;

import com.mrbpurnachandra.sponews.model.Article;
import com.mrbpurnachandra.sponews.model.Author;
import com.mrbpurnachandra.sponews.repository.ArticleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }


    public Article save(Article article) {
        return articleRepository.save(article);
    }

    public Optional<Article> findById(Long articleId) {
        return articleRepository.findById(Long.valueOf(articleId));
    }

    public Iterable<Article> getArticlesForHome() {
        return articleRepository.findTop10ByOrderByPublishedOnDesc();
    }

    public Iterable<Article> findArticlesByAuthor(Author author) {
        return articleRepository.findTop10ArticlesByAuthorOrderByPublishedOnDesc(author);
    }

    public Iterable<Article> findArticlesMatchingName(String name) {
        String queryName = "%" + name + "%";

        return articleRepository.findTop10ByTitleLikeIgnoreCase(queryName);
    }
}
