package com.mrbpurnachandra.sponews.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 4, message = "Title should be atleast 4 characters.")
    private String title;

    @NotNull
    @Size(min=64, max = 2048, message = "Content should be between 64 and 2048 characters.")
    @Lob
    private String content;

    private Date publishedOn = new Date();
}
