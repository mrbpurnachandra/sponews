package com.mrbpurnachandra.sponews.controller;

import com.mrbpurnachandra.sponews.dto.CommentDTO;
import com.mrbpurnachandra.sponews.exception.ArticleNotFoundException;
import com.mrbpurnachandra.sponews.model.Article;
import com.mrbpurnachandra.sponews.model.Author;
import com.mrbpurnachandra.sponews.model.Comment;
import com.mrbpurnachandra.sponews.service.ArticleService;
import com.mrbpurnachandra.sponews.service.AuthorService;
import com.mrbpurnachandra.sponews.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Controller
public class ArticleController {
    private final ArticleService articleService;
    private final AuthorService authorService;
    private final CommentService commentService;

    @Autowired
    public ArticleController(ArticleService articleService, AuthorService authorService, CommentService commentService) {
        this.articleService = articleService;
        this.authorService = authorService;
        this.commentService = commentService;
    }

    @GetMapping("/article/{articleId}")
    public String show(@PathVariable Long articleId, Model model, RedirectAttributes redirectAttributes) {
        Optional<Article> optionalArticle = articleService.findById(articleId);
        if(optionalArticle.isEmpty()) {
            redirectAttributes.addFlashAttribute("warning", "पृष्ठ फेला परेन");
            return "redirect:/";
        }

        Article article = optionalArticle.get();
        Comment comment = new Comment();
        comment.setArticle(article);

        model.addAttribute("article", article);
        model.addAttribute("comment", comment);

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

    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/article/{articleId}/comment")
    public CommentDTO add(@Valid Comment comment, @PathVariable Long articleId, OAuth2AuthenticationToken authentication) {
        OAuth2User user = authentication.getPrincipal();

        Optional<Article> optionalArticle = articleService.findById(articleId);
        if(optionalArticle.isEmpty()) {
            throw new ArticleNotFoundException();
        }

        comment.setArticle(optionalArticle.get());
        comment.setEmail(user.getAttribute("email"));
        comment.setName(user.getAttribute("name"));
        Comment savedComment = commentService.save(comment);

        return new CommentDTO(savedComment);
    }

    @ResponseBody
    @GetMapping("/article/{articleId}/comment")
    public List<CommentDTO> index() {
        List<CommentDTO> comments = new ArrayList<>();
        for (var comment: commentService.findAll()) {
            comments.add(new CommentDTO(comment));
        }

        return comments;
    }

}
