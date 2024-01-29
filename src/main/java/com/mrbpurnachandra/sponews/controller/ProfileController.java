package com.mrbpurnachandra.sponews.controller;

import com.mrbpurnachandra.sponews.model.Author;
import com.mrbpurnachandra.sponews.service.ArticleService;
import com.mrbpurnachandra.sponews.service.AuthorService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class ProfileController {
    private final AuthorService authorService;
    private final ArticleService articleService;

    public ProfileController(AuthorService authorService, ArticleService articleService, ArticleService articleService1) {
        this.authorService = authorService;
        this.articleService = articleService1;
    }

    @GetMapping("/profile")
    public String index(
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            OAuth2AuthenticationToken authentication,
            Model model
    ) {
        String email = authentication.getPrincipal().getAttribute("email");
        Optional<Author> optionalAuthor = authorService.findAuthorByEmail(email);

        optionalAuthor.ifPresent(author -> {
            model.addAttribute("author", author);

            Pageable pageable = Pageable.ofSize(size).withPage(page);

            model.addAttribute("articles", articleService.findArticlesByAuthor(author, pageable));
        });

        return "profile";
    }
}
