package com.mrbpurnachandra.sponews.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 4, max = 1024, message = "Content should be between 4 and 1024 characters.")
    @Lob
    private String content;

    private String email;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private Article article;

    private Date publishedOn = new Date();

}
