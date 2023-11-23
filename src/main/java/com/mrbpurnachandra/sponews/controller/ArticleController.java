package com.mrbpurnachandra.sponews.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ArticleController {
    @GetMapping("/article/{articleId}")
    public String show() {
        return "article/show";
    }

    @GetMapping("/article/create")
    public String create() {
        return "article/create"; 
    }
}
