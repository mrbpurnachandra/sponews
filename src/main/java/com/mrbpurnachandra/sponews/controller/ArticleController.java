package com.mrbpurnachandra.sponews.controller;

import com.mrbpurnachandra.sponews.model.Article;
import com.mrbpurnachandra.sponews.model.Author;
import com.mrbpurnachandra.sponews.service.ArticleService;
import com.mrbpurnachandra.sponews.service.AuthorService;
import jakarta.validation.Valid;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;


@Controller
public class ArticleController {
    private final ArticleService articleService;
    private final AuthorService authorService;

    public ArticleController(ArticleService articleService, AuthorService authorService) {
        this.articleService = articleService;
        this.authorService = authorService;
    }

    @GetMapping("/article/{articleId}")
    public String show(@PathVariable Integer articleId, Model model, RedirectAttributes redirectAttributes) {
        Optional<Article> article = articleService.findById(articleId);
        if(article.isEmpty()) {
            redirectAttributes.addFlashAttribute("warning", "पृष्ठ फेला परेन");
            return "redirect:/";
        }

        model.addAttribute("article", article.get());
        return "article/show";
    }

    @GetMapping("/article/create")
    public String create(Model model, OAuth2AuthenticationToken authentication) {
        if(!model.containsAttribute("article")) {
            model.addAttribute("article", new Article());
        }

        String email = authentication.getPrincipal().getAttribute("email");
        if(!authorService.isAuthorRegistered(email)) {
            return "redirect:/profile";
        }

        return "article/create";
    }

    @PostMapping("/article")
    public String add(@Valid Article article, BindingResult result, OAuth2AuthenticationToken authentication, RedirectAttributes redirectAttributes) {
        if(result.hasErrors()) {
            Map<String, Object> modelMap = result.getModel();
            modelMap.forEach(redirectAttributes::addFlashAttribute);

            return "redirect:article/create";
        }

        String email = authentication.getPrincipal().getAttribute("email");
        Optional<Author> optionalAuthor = authorService.findAuthorByEmail(email);
        if(optionalAuthor.isEmpty()) {
            return "redirect:/profile";
        }

        article.setAuthor(optionalAuthor.get());

        Article savedArticle = articleService.save(article);
        redirectAttributes.addFlashAttribute("info", "लेख डाटाबेसमा बचत गरियो");
        return "redirect:/article/" + savedArticle.getId();
    }

}
