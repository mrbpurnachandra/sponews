package com.mrbpurnachandra.sponews.controller;

import com.mrbpurnachandra.sponews.model.Author;
import com.mrbpurnachandra.sponews.service.AuthorService;
import jakarta.validation.Valid;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/author/create")
    public String create(@ModelAttribute Author author, OAuth2AuthenticationToken authentication) {
        String email = authentication.getPrincipal().getAttribute("email");
        Optional<Author> optionalAuthor = authorService.findAuthorByEmail(email);
        if (optionalAuthor.isPresent()) {
            return "redirect:/profile";
        }

        return "author/create";
    }

    @PostMapping("/author/create")
    public String add(@Valid Author author, BindingResult result, OAuth2AuthenticationToken authentication, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "author/create";
        }

        String email = authentication.getPrincipal().getAttribute("email");
        Optional<Author> optionalAuthor = authorService.findAuthorByEmail(email);
        if (optionalAuthor.isEmpty()) {
            author.setEmail(email);
            authorService.save(author);

            redirectAttributes.addFlashAttribute("info", "लेखक प्रोफाइल सिर्जना भयो");
        }

        return "redirect:/profile";
    }

    @GetMapping("/author/{id}")
    public String show() {
        return "author/show";
    }
}
