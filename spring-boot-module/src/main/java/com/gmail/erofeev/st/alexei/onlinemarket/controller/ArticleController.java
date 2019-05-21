package com.gmail.erofeev.st.alexei.onlinemarket.controller;

import com.gmail.erofeev.st.alexei.onlinemarket.controller.util.Paginator;
import com.gmail.erofeev.st.alexei.onlinemarket.service.ArticleService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.CommentService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.AppUserPrincipal;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ArticleDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.CommentDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.PageDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.SearchingFilter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ArticleController {
    private final ArticleService articleService;
    private final CommentService commentService;

    public ArticleController(ArticleService articleService, CommentService commentService) {
        this.articleService = articleService;
        this.commentService = commentService;
    }

    @GetMapping("/articles")
    public String getArticles(Model model,
                              @RequestParam(defaultValue = "1", required = false) String page,
                              @RequestParam(defaultValue = "10", required = false) String size,
                              @RequestParam(required = false) String tag,
                              @ModelAttribute SearchingFilter searchingFilter) {
        Paginator paginator = new Paginator(page, size);
        searchingFilter.setTag(tag);
        PageDTO<ArticleDTO> pageDTO = articleService.getArticles(paginator.getPage(), paginator.getSize(), searchingFilter);
        paginator.setMaxPage(pageDTO.getAmountOfPages());
        model.addAttribute("paginator", paginator);
        model.addAttribute("articles", pageDTO.getList());
        model.addAttribute("searchingFilter", new SearchingFilter());
        return "articles";
    }

    @PostMapping("/articles/{id}")
    public String getArticle(@PathVariable Long id, Model model) {
        ArticleDTO article = articleService.getArticleById(id);
        model.addAttribute("article", article);
        CommentDTO newComment = new CommentDTO();
        newComment.setArticleId(id);
        model.addAttribute("newComment", newComment);
        return "article";
    }

    @PostMapping("/articles/{id}/new")
    public String saveNewComment(@ModelAttribute("newComment") CommentDTO commentDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUserPrincipal principal = (AppUserPrincipal) authentication.getPrincipal();
        Long userId = principal.getUser().getId();
        commentService.save(userId, commentDTO);
        return "redirect:/articles";
    }
}
