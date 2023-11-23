package com.mrbpurnachandra.sponews.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @GetMapping
    public String index() {
        return "home"; 

    }

    @GetMapping("/secured") 
    public String secured() {
        return "secured";
    }
}
