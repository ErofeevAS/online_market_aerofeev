package com.gmail.erofeev.st.alexei.onlinemarket.controller;

import com.gmail.erofeev.st.alexei.onlinemarket.service.ArticleService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.AppUserPrincipal;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ArticleDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ArticleRestDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/articles")
public class RestArticlesController {

    private final ArticleService articleService;

    public RestArticlesController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public List<ArticleRestDTO> getArticles() {
        return articleService.getRestArticles();
    }

    @GetMapping("/{id}")
    public ArticleDTO getArticles(@PathVariable Long id) {
        return articleService.getArticleById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteArticle(@PathVariable Long id) {
        return articleService.delete(id);
    }

    @PostMapping
    public ResponseEntity addArticle(@RequestBody ArticleDTO articleDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUserPrincipal principal = (AppUserPrincipal) authentication.getPrincipal();
        Long id = principal.getUser().getId();
        UserDTO user = new UserDTO();
        user.setId(id);
        articleDTO.setUser(user);
        articleService.add(articleDTO);
        return new ResponseEntity(HttpStatus.CREATED);
    }
}
