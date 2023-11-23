package com.mrbpurnachandra.sponews.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthorController {
    @GetMapping("/author/create")
    public String create() {
        return "author/create";
    }

    @GetMapping("/author/{id}")
    public String show() {
        return "author/show";
    }
}
