package com.mrbpurnachandra.sponews.service;

import com.mrbpurnachandra.sponews.model.Article;
import com.mrbpurnachandra.sponews.repository.ArticleRepository;
import org.springframework.stereotype.Service;

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

    public Optional<Article> findById(Integer articleId) {
        return articleRepository.findById(Long.valueOf(articleId));
    }

    public Iterable<Article> getArticlesForHome() {
        return articleRepository.findTop10ByOrderByPublishedOnDesc();
    }
}
