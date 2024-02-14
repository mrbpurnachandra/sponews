package com.mrbpurnachandra.sponews.repository;

import com.mrbpurnachandra.sponews.model.Article;
import com.mrbpurnachandra.sponews.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CommentRepository extends CrudRepository<Comment, Long>, PagingAndSortingRepository<Comment, Long> {
    Page<Comment> findAllByArticleOrderByPublishedOnDesc(Article article, Pageable pageable);

    @Query("select c.emotion, count(c) from Comment c where c.article.id = :id group by c.emotion")
    List<Integer[]> getStatistics(Long id);
}
