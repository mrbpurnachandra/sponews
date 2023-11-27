package com.mrbpurnachandra.sponews.controller;

import com.mrbpurnachandra.sponews.model.Author;
import com.mrbpurnachandra.sponews.service.ArticleService;
import com.mrbpurnachandra.sponews.service.AuthorService;
import jakarta.validation.Valid;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Controller
public class AuthorController {
    private final AuthorService authorService;
    private final ArticleService articleService;

    public AuthorController(AuthorService authorService, ArticleService articleService) {
        this.authorService = authorService;
        this.articleService = articleService;
    }

    @GetMapping("/author/create")
    public String create(Model model, OAuth2AuthenticationToken authentication) {
        if(!model.containsAttribute("author")) {
            model.addAttribute("author", new Author());
        }

        String email = authentication.getPrincipal().getAttribute("email");
        if (authorService.isAuthorRegistered(email)) {
            return "redirect:/profile";
        }

        return "author/create";
    }

    @PostMapping("/author")
    public String add(@Valid Author author, BindingResult result, OAuth2AuthenticationToken authentication, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            Map<String, Object> modelMap = result.getModel();
            modelMap.forEach(redirectAttributes::addFlashAttribute);

            return "redirect:author/create";
        }

        String email = authentication.getPrincipal().getAttribute("email");
        if(!authorService.isAuthorRegistered(email)) {
            author.setEmail(email);
            authorService.save(author);

            redirectAttributes.addFlashAttribute("info", "लेखक प्रोफाइल सिर्जना भयो");
        }

        return "redirect:/profile";
    }

    @GetMapping("/author/{authorId}")
    public String show(@PathVariable Integer authorId, RedirectAttributes redirectAttributes, Model model) {
        Optional<Author> authorOptional = authorService.findById(authorId);
        if(authorOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("warning", "पृष्ठ फेला परेन");
            return "redirect:/";
        }

        Author author = authorOptional.get();
        model.addAttribute("author", author);
        model.addAttribute("articles", articleService.findArticlesByAuthor(author));
        return "author/show";
    }
}
