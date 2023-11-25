package com.mrbpurnachandra.sponews.controller;

import com.mrbpurnachandra.sponews.model.Article;
import com.mrbpurnachandra.sponews.service.ArticleService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;


@Controller
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
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
    public String create(@ModelAttribute Article article) {
        return "article/create";
    }

    @PostMapping("/article/create")
    public String add(@Valid Article article, BindingResult result, RedirectAttributes redirectAttributes) {
        if(result.hasErrors()) {
            return "article/create";
        }

        Article savedArticle = articleService.save(article);
        redirectAttributes.addFlashAttribute("info", "लेख डाटाबेसमा बचत गरियो");
        return "redirect:/article/" + savedArticle.getId();
    }

}
