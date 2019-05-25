package com.gmail.erofeev.st.alexei.onlinemarket.controller;

import com.gmail.erofeev.st.alexei.onlinemarket.controller.util.DateTimeLocaleUtil;
import com.gmail.erofeev.st.alexei.onlinemarket.controller.util.Paginator;
import com.gmail.erofeev.st.alexei.onlinemarket.service.ArticleService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.CommentService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.AppUserPrincipal;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ArticleDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.CommentDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.NewArticleDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.PageDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.SearchingFilter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
public class ArticleController {
    private final ArticleService articleService;
    private final CommentService commentService;
    private final DateTimeLocaleUtil dateTimeLocaleUtil;

    public ArticleController(ArticleService articleService, CommentService commentService, DateTimeLocaleUtil dateTimeLocaleUtil) {
        this.articleService = articleService;
        this.commentService = commentService;
        this.dateTimeLocaleUtil = dateTimeLocaleUtil;
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

    @GetMapping("/articles/tag/{tag}")
    public String getArticlesByTag(Model model,
                                   @RequestParam(defaultValue = "1", required = false) String page,
                                   @RequestParam(defaultValue = "10", required = false) String size,
                                   @PathVariable String tag,
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


    @GetMapping("/articles/{id}")
    public String getArticleAfterUpdateComment(@PathVariable Long id,
                                               Model model,
                                               Authentication authentication) {
        ArticleDTO article = articleService.getArticleById(id);
        model.addAttribute("article", article);
        Long articleAuthorId = article.getUser().getId();
        Long userId = getSecureUserId(authentication);
        Boolean showDeleteButton = false;
        if (articleAuthorId == userId) {
            showDeleteButton = true;
        }
        model.addAttribute("showDeleteButton", showDeleteButton);
        CommentDTO newComment = new CommentDTO();
        newComment.setArticleId(id);
        model.addAttribute("newComment", newComment);
        NewArticleDTO editedArticle = new NewArticleDTO();
        model.addAttribute("editedArticle", editedArticle);
        return "article";
    }

    @PostMapping("/articles/{id}/new")
    public String saveNewComment(@ModelAttribute("newComment") CommentDTO commentDTO,
                                 @PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUserPrincipal principal = (AppUserPrincipal) authentication.getPrincipal();
        Long userId = principal.getUser().getId();
        commentService.save(userId, commentDTO);
        return "redirect:/articles/" + id;
    }

    @PostMapping("/articles/{id}/deleteComment")
    public String deleteComment(
            @PathVariable Long id,
            @RequestParam("deleteCommentId") Long commentId) {
        commentService.deleteComment(commentId);
        return "redirect:/articles/" + id;
    }

    @GetMapping("/articles/new")
    public String createArticle(Model model) {
        NewArticleDTO article = new NewArticleDTO();
        model.addAttribute("article", article);
        String currentDateTime = dateTimeLocaleUtil.getCurrentTimeInDateTimeLocaleFormat();
        model.addAttribute("currentDate", currentDateTime);
        return "newArticle";
    }

    @PostMapping("/articles/new")
    public String createArticle(
            Model model,
            @ModelAttribute("article") @Valid NewArticleDTO article,
            BindingResult bindingResult,
            Authentication authentication) {
        if (bindingResult.hasErrors()) {
            return "newArticle";
        }
        Long userId = getSecureUserId(authentication);
        article.setUserId(userId);
        articleService.createArticle(article);
        model.addAttribute("info", "article was created");
        model.addAttribute("article", article);
        return "newArticle";
    }


    @PostMapping("/articles/{id}/update")
    public String updateArticle(@PathVariable Long id,
                                @ModelAttribute("editedArticle") @Validated NewArticleDTO article,
                                BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            return "redirect:/articles/" + id;
//        }
        articleService.update(article);
        return "redirect:/articles/" + id;
    }

    private Long getSecureUserId(Authentication authentication) {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUserPrincipal principal = (AppUserPrincipal) authentication.getPrincipal();
        return principal.getUser().getId();
    }
}
