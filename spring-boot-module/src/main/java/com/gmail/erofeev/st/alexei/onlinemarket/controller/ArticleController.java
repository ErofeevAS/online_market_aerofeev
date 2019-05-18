package com.gmail.erofeev.st.alexei.onlinemarket.controller;

import com.gmail.erofeev.st.alexei.onlinemarket.service.ArticleService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ArticleDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/articles")
    public String getArticles(Model model) {
        List<ArticleDTO> articles = articleService.getAll();
        articleService.getArticles(3, 3);
        model.addAttribute("articles", articles);
        return "articles";
    }

    @PostMapping("/articles/{id}")
    public String getArticle(@PathVariable Long id, Model model) {
        ArticleDTO article = articleService.getArticleById(id);
        model.addAttribute("article", article);
        return "article";
    }
}
