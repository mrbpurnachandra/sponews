package com.mrbpurnachandra.sponews.service;

import com.mrbpurnachandra.sponews.model.Article;
import com.mrbpurnachandra.sponews.model.ArticleStatistics;
import com.mrbpurnachandra.sponews.model.Comment;
import com.mrbpurnachandra.sponews.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    public Page<Comment> findAllForArticle(Long articleId, Pageable pageable) {
        Article article = new Article();
        article.setId(articleId);

        return commentRepository.findAllByArticleOrderByPublishedOnDesc(article, pageable);
    }

    public ArticleStatistics getArticleStatistics(Long id) {
        List<Integer[]> result = commentRepository.getStatistics(id);
        Map<Integer, Integer> map = new HashMap<>();

        result.forEach(integers -> {
            map.put(integers[0], integers[1]);
        });

        return new ArticleStatistics(map.get(0), map.get(1), map.get(2), map.get(3), map.get(4), map.get(null));
    }
}
