package com.mrbpurnachandra.sponews.controller;

import com.mrbpurnachandra.sponews.model.Article;
import com.mrbpurnachandra.sponews.service.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private ArticleService articleService;

    public HomeController(ArticleService articleService) {

        this.articleService = articleService;
    }
    
    @GetMapping
    public String index(Model model) {
        Iterable<Article> articles = articleService.getArticlesForHome();
        model.addAttribute("articles", articles);

        return "home";

    }

    @GetMapping("/secured") 
    public String secured() {
        return "secured";
    }
}
