package com.mrbpurnachandra.sponews.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"id", "email"})
)
@Data
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String email;

    @NotNull
    @Size(min = 4, message = "Name should be at least 4 characters.")
    private String name;

    @NotNull
    @Size(min = 16, max= 2048, message = "Description should be between 64 and 2048 characters.")
    @Column(length = 2048)
    private String description;

    private Date createdOn = new Date();

    @Transient
    @OneToMany(mappedBy = "author")
    private List<Article> articles = new ArrayList<>();
}
