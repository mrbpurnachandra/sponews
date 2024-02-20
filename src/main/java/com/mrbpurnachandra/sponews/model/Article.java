package com.mrbpurnachandra.sponews.model;

import com.mrbpurnachandra.sponews.util.HtmlParser;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Entity
@Data
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Author author;

    @NotNull
    @Size(min = 4, message = "Title should be at least 4 characters.")
    private String title;

    @NotNull
    @Size(min=64, max = 2048, message = "Content should be between 64 and 2048 characters.")
    @Column(length = 2048)
    private String content;

    @Transient
    @OneToMany(mappedBy = "article")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "article")
    private List<Tag> tags = new ArrayList<>();

    private Date publishedOn = new Date();

    public String getShortContent() {
        String content = HtmlParser.extractText(this.content);
        int contentLength = content.length();

        int limit = Math.min(contentLength - 1, 120);

        content = content.substring(0, limit);

        if (limit < contentLength) content += "...";

        return content;
    }

    public String getFormattedDate() {
        var formatter = new SimpleDateFormat("EEE, d MMM yyyy", Locale.of("ne"));

        return formatter.format(this.publishedOn);
    }
}
