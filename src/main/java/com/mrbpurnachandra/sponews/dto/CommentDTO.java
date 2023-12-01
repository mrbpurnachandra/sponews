package com.mrbpurnachandra.sponews.dto;

import com.mrbpurnachandra.sponews.model.Comment;
import lombok.Data;

@Data
public class CommentDTO {
    public CommentDTO(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.email = comment.getEmail();
        this.name = comment.getName();
        this.articleId = comment.getArticle().getId();
    }

    private Long id;
    private String content;
    private String email;
    private String name;
    private Long articleId;

}
