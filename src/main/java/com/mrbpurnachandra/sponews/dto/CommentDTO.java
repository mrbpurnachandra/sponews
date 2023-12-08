package com.mrbpurnachandra.sponews.dto;

import com.mrbpurnachandra.sponews.model.Comment;
import lombok.Data;

@Data
public class CommentDTO {
    private final Long id;
    private final String content;
    private final String email;
    private final String name;
    private final Long articleId;
    private final Comment.Emotion emotion;

    public CommentDTO(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.email = comment.getEmail();
        this.name = comment.getName();
        this.articleId = comment.getArticle().getId();
        this.emotion = comment.getEmotion();
    }
}
