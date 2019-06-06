package com.gmail.erofeev.st.alexei.onlinemarket.controller;

import com.gmail.erofeev.st.alexei.onlinemarket.controller.util.FrontEndValidator;
import com.gmail.erofeev.st.alexei.onlinemarket.controller.util.Paginator;
import com.gmail.erofeev.st.alexei.onlinemarket.service.ArticleService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.CommentService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.DateTimeLocaleService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.UserAuthenticationService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.impl.DateTimeLocaleServiceImpl;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ArticleDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.CommentDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.NewArticleDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.PageDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.SearchingFilter;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

import static com.gmail.erofeev.st.alexei.onlinemarket.config.properties.GlobalConstants.MAX_COMMENT_LENGTH;

@Controller
public class ArticleController {
    private final ArticleService articleService;
    private final CommentService commentService;
    private final DateTimeLocaleService dateTimeLocaleService;
    private final UserAuthenticationService userAuthenticationService;
    private final FrontEndValidator frontEndValidator;

    public ArticleController(ArticleService articleService,
                             CommentService commentService,
                             DateTimeLocaleServiceImpl dateTimeLocaleUtil,
                             UserAuthenticationService userAuthenticationService,
                             FrontEndValidator frontEndValidator) {
        this.articleService = articleService;
        this.commentService = commentService;
        this.dateTimeLocaleService = dateTimeLocaleUtil;
        this.userAuthenticationService = userAuthenticationService;
        this.frontEndValidator = frontEndValidator;
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
    public String getArticleAfterUpdateComment(@PathVariable String id,
                                               Model model) {
        Long validatedId = frontEndValidator.validateId(id);
        ArticleDTO article = articleService.getArticleById(validatedId);
        model.addAttribute("article", article);
        CommentDTO newComment = new CommentDTO();
        newComment.setArticleId(validatedId);
        model.addAttribute("newComment", newComment);
        NewArticleDTO editedArticle = new NewArticleDTO();
        model.addAttribute("editedArticle", editedArticle);
        return "article";
    }

    @PostMapping("/articles/{id}/newComment")
    public String saveNewComment(
            @ModelAttribute("newComment") CommentDTO commentDTO,
            @PathVariable Long id,
            Authentication authentication,
            RedirectAttributes attributes) {
        if (!frontEndValidator.isTextShorterThan(commentDTO.getContent(), MAX_COMMENT_LENGTH)) {
            attributes.addFlashAttribute("info", "comment must be less than " + MAX_COMMENT_LENGTH);
            return "redirect:/articles/" + id;
        }
        Long userId = userAuthenticationService.getSecureUserId(authentication);
        commentService.save(userId, commentDTO);
        return "redirect:/articles/" + id;
    }

    @PostMapping("/articles/{id}/deleteComment")
    public String deleteComment(@PathVariable Long id,
                                @RequestParam("deleteCommentId") Long commentId) {
        commentService.deleteComment(commentId);
        return "redirect:/articles/" + id;
    }

    @PostMapping("/articles/{id}/delete")
    public String deleteArticle(@PathVariable Long id) {
        articleService.deleteArticleById(id);
        return "redirect:/articles";
    }

    @GetMapping("/articles/sale/new")
    public String createArticle(Model model) {
        NewArticleDTO article = new NewArticleDTO();
        model.addAttribute("article", article);
        String currentDateTime = dateTimeLocaleService.getCurrentTimeInDateTimeLocaleFormat();
        model.addAttribute("currentDate", currentDateTime);
        return "newArticle";
    }

    @PostMapping("/articles/sale/new")
    public String createArticle(Model model,
                                @ModelAttribute("article") @Valid NewArticleDTO article,
                                BindingResult bindingResult,
                                Authentication authentication) {
        String currentDateTime = dateTimeLocaleService.getCurrentTimeInDateTimeLocaleFormat();
        model.addAttribute("currentDate", currentDateTime);
        if (bindingResult.hasErrors()) {
            return "newArticle";
        }
        Long userId = userAuthenticationService.getSecureUserId(authentication);
        article.setUserId(userId);
        articleService.createArticle(article);
        model.addAttribute("info", "article was created");
        model.addAttribute("article", article);
        return "newArticle";
    }

    @PostMapping("/articles/{id}/update")
    public String updateArticle(@PathVariable Long id,
                                @ModelAttribute("editedArticle") @Valid NewArticleDTO article,
                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/articles/" + id;
        }
        articleService.update(article);
        return "redirect:/articles/" + id;
    }
}
