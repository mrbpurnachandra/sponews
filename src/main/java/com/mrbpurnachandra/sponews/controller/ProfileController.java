package com.mrbpurnachandra.sponews.controller;

import com.mrbpurnachandra.sponews.model.Author;
import com.mrbpurnachandra.sponews.service.ArticleService;
import com.mrbpurnachandra.sponews.service.AuthorService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
    public String index(OAuth2AuthenticationToken authentication, Model model) {
        String email = authentication.getPrincipal().getAttribute("email");
        Optional<Author> optionalAuthor = authorService.findAuthorByEmail(email);

        optionalAuthor.ifPresent(author -> {
            model.addAttribute("author", author);
            model.addAttribute("articles", articleService.findArticlesByAuthor(author));
        });

        return "profile";
    }
}
