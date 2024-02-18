package com.mrbpurnachandra.sponews.service;

import com.mrbpurnachandra.sponews.exception.ArticleNotFoundException;
import com.mrbpurnachandra.sponews.model.Article;
import com.mrbpurnachandra.sponews.model.ArticleStatistics;
import com.mrbpurnachandra.sponews.model.Author;
import com.mrbpurnachandra.sponews.model.Tag;
import com.mrbpurnachandra.sponews.repository.ArticleRepository;
import com.mrbpurnachandra.sponews.repository.TagRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final CommentService commentService;
    private final TagRepository tagRepository;

    public ArticleService(ArticleRepository articleRepository, CommentService commentService,
                          TagRepository tagRepository) {
        this.articleRepository = articleRepository;
        this.commentService = commentService;
        this.tagRepository = tagRepository;
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

    public Page<Article> findArticlesByAuthor(Author author, Pageable pageable) {
        return articleRepository.findArticlesByAuthorOrderByPublishedOnDesc(author, pageable);
    }

    public ArticleStatistics getArticleStatistics(Long id, Author author) {
        Optional<Article> optionalArticle = findById(id);

        Article article = optionalArticle.orElseThrow(ArticleNotFoundException::new);

        if (!article.getAuthor().equals(author)) {
            throw new AccessDeniedException("Unauthorized action");
        }

        return commentService.getArticleStatistics(id);
    }

    public Iterable<Article> findArticlesMatchingName(String name) {
        String queryName = "%" + name + "%";

        return articleRepository.findTop10ByTitleLikeIgnoreCase(queryName);
    }

    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }
}
