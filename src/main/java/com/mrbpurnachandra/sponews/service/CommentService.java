package com.mrbpurnachandra.sponews.service;

import com.mrbpurnachandra.sponews.model.Comment;
import com.mrbpurnachandra.sponews.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Iterable<Comment> findAll() {
        return commentRepository.findTop10ByOrderByPublishedOnDesc();
    }
}
