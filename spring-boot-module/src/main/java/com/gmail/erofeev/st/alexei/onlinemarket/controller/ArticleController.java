package com.gmail.erofeev.st.alexei.onlinemarket.controller;

import com.gmail.erofeev.st.alexei.onlinemarket.controller.util.CustomWrapper;
import com.gmail.erofeev.st.alexei.onlinemarket.controller.util.Paginator;
import com.gmail.erofeev.st.alexei.onlinemarket.service.ArticleService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ArticleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.Positive;
import java.util.List;

@Controller
public class ArticleController {

    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/articles")
    public String getArticles(Model model,
                              @RequestParam(defaultValue = "1", required = false) @Positive int page,
                              @RequestParam(defaultValue = "10", required = false) @Positive int size) {
        Integer maxPage = articleService.getAmount(size);
        Paginator paginator = new Paginator(page, maxPage, size);
        List<ArticleDTO> articles = articleService.getArticles(page, size);
        model.addAttribute("articles", articles);
        model.addAttribute("paginator", paginator);
        CustomWrapper changes = new CustomWrapper();
        model.addAttribute("changes", changes);
        return "articles";
    }

    @PostMapping("/articles/update")
    public String updateArticles(@ModelAttribute("changes") CustomWrapper changes) {
        System.out.println(changes);
        return "articles";
    }

    @PostMapping("/articles/{id}/delete")
    public String deleteArticlesPost(@PathVariable Long id) {
//        articleService.delete(id);
        return "redirect:/articles";
    }
}
