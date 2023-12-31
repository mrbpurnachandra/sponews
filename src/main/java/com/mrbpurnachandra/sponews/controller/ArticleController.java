package com.mrbpurnachandra.sponews.controller;

import com.mrbpurnachandra.sponews.dto.CommentDTO;
import com.mrbpurnachandra.sponews.dto.EmotionPredictionRequestDTO;
import com.mrbpurnachandra.sponews.dto.EmotionPredictionResponseDTO;
import com.mrbpurnachandra.sponews.exception.ArticleNotFoundException;
import com.mrbpurnachandra.sponews.model.Article;
import com.mrbpurnachandra.sponews.model.Author;
import com.mrbpurnachandra.sponews.model.Comment;
import com.mrbpurnachandra.sponews.props.EmotionPredictionProps;
import com.mrbpurnachandra.sponews.service.ArticleService;
import com.mrbpurnachandra.sponews.service.AuthorService;
import com.mrbpurnachandra.sponews.service.CommentService;
import com.mrbpurnachandra.sponews.util.HTMLSanitizer;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.DateFormat;
import java.util.*;


@Controller
public class ArticleController {
    private final ArticleService articleService;
    private final AuthorService authorService;
    private final CommentService commentService;
    private final DateFormat dateFormat;
    private final WebClient client;

    private final EmotionPredictionProps predictionProps;

    @Autowired
    public ArticleController(ArticleService articleService, AuthorService authorService, CommentService commentService, DateFormat dateFormat, WebClient client, EmotionPredictionProps predictionProps) {
        this.articleService = articleService;
        this.authorService = authorService;
        this.commentService = commentService;
        this.dateFormat = dateFormat;
        this.client = client;
        this.predictionProps = predictionProps;
    }

    @GetMapping("/article/{articleId}")
    public String show(@PathVariable Long articleId, Model model, RedirectAttributes redirectAttributes) {
        Optional<Article> optionalArticle = articleService.findById(articleId);
        if (optionalArticle.isEmpty()) {
            redirectAttributes.addFlashAttribute("warning", "पृष्ठ फेला परेन");
            return "redirect:/";
        }

        Article article = optionalArticle.get();
        String formattedDate = dateFormat.format(article.getPublishedOn());

        Comment comment = new Comment();
        comment.setArticle(article);

        model.addAttribute("article", article);
        model.addAttribute("comment", comment);
        model.addAttribute("formattedDate", formattedDate);

        return "article/show";
    }

    @GetMapping("/article/create")
    public String create(Model model, OAuth2AuthenticationToken authentication) {
        if (!model.containsAttribute("article")) {
            model.addAttribute("article", new Article());
        }

        String email = authentication.getPrincipal().getAttribute("email");
        if (!authorService.isAuthorRegistered(email)) {
            return "redirect:/profile";
        }

        return "article/create";
    }

    @PostMapping("/article")
    public String add(@Valid Article article, BindingResult result, OAuth2AuthenticationToken authentication, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            Map<String, Object> modelMap = result.getModel();
            modelMap.forEach(redirectAttributes::addFlashAttribute);

            return "redirect:article/create";
        }

        String email = authentication.getPrincipal().getAttribute("email");
        Optional<Author> optionalAuthor = authorService.findAuthorByEmail(email);
        if (optionalAuthor.isEmpty()) {
            return "redirect:/profile";
        }

        article.setAuthor(optionalAuthor.get());

        // Sanitize the HTML before saving the article
        article.setContent(HTMLSanitizer.sanitize(article.getContent()));

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
        if (optionalArticle.isEmpty()) {
            throw new ArticleNotFoundException();
        }

        comment.setArticle(optionalArticle.get());
        comment.setEmail(user.getAttribute("email"));
        comment.setName(user.getAttribute("name"));

        // Attempt to find emotion from api
        EmotionPredictionResponseDTO response = getEmotionPredictionResponseDTO(comment);

        Comment.Emotion emotion = null;
        if (response != null) {
            emotion = Comment.Emotion.values()[response.result()];
        }
        comment.setEmotion(emotion);

        Comment savedComment = commentService.save(comment);

        return new CommentDTO(savedComment);
    }

    private EmotionPredictionResponseDTO getEmotionPredictionResponseDTO(Comment comment) {
        EmotionPredictionResponseDTO responseDTO = null;
        try {
            responseDTO =  client
                    .post()
                    .uri(predictionProps.getUri())
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new EmotionPredictionRequestDTO(comment.getContent()))
                    .retrieve()
                    .bodyToMono(EmotionPredictionResponseDTO.class)
                    .block();
        } catch (Exception e) {
            System.out.println("Error fetching emotion: "+ e.getMessage());
        }

        return responseDTO;
    }

    @ResponseBody
    @GetMapping("/article/{articleId}/comment")
    public Map<String, Object> index(@PathVariable Long articleId, @RequestParam Integer page) {
        Pageable pageable = PageRequest.of(page, 5);

        List<CommentDTO> comments = new ArrayList<>();

        Page<Comment> commentPage = commentService.findAllForArticle(articleId, pageable);

        for (var comment : commentPage) {
            comments.add(new CommentDTO(comment));
        }

        Map<String, Object> map = new HashMap<>();
        map.put("data", comments);
        map.put("hasNext", commentPage.hasNext());

        return map;
    }

}
