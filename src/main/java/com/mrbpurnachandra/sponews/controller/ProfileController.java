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

    public ProfileController(AuthorService authorService, ArticleService articleService) {
        this.authorService = authorService;
    }

    @GetMapping("/profile")
    public String index(OAuth2AuthenticationToken authentication, Model model) {
        String email = authentication.getPrincipal().getAttribute("email");
        Optional<Author> optionalAuthor = authorService.findAuthorByEmail(email);
        optionalAuthor.ifPresent(author -> model.addAttribute("author", author));

        return "profile";
    }
}
