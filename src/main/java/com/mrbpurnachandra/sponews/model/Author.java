package com.mrbpurnachandra.sponews.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

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
    @Size(min = 16, max= 2048, message = "Description should be between 64 and 2048 characters.")
    @Lob
    private String description;

    Date createdOn = new Date();
}
