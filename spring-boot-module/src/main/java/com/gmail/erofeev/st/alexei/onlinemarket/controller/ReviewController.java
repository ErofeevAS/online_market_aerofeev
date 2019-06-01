package com.gmail.erofeev.st.alexei.onlinemarket.controller;

import com.gmail.erofeev.st.alexei.onlinemarket.controller.util.Paginator;
import com.gmail.erofeev.st.alexei.onlinemarket.service.ReviewService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.UserAuthenticationService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.PageDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ReviewDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ReviewsListWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ReviewController {
    private final ReviewService reviewService;
    private final UserAuthenticationService userAuthenticationService;

    @Autowired
    public ReviewController(ReviewService reviewService, UserAuthenticationService userAuthenticationService) {
        this.reviewService = reviewService;
        this.userAuthenticationService = userAuthenticationService;
    }

    @GetMapping("/reviews")
    public String getReviews(Model model,
                             @RequestParam(defaultValue = "1", required = false) String page,
                             @RequestParam(defaultValue = "10", required = false) String size) {
        Paginator paginator = new Paginator(page, size);
        PageDTO<ReviewDTO> pageDTO = reviewService.getReviews(paginator.getPage(), paginator.getSize());
        paginator.setMaxPage(pageDTO.getAmountOfPages());
        model.addAttribute("paginator", paginator);
        ReviewsListWrapper reviewsChanges = new ReviewsListWrapper(pageDTO.getList());
        model.addAttribute("reviewsChanges", reviewsChanges);
        return "reviews";
    }

    @PostMapping("/reviews/{id}/delete")
    public String deleteReview(@PathVariable Long id) {
        reviewService.delete(id);
        return "redirect:/reviews";
    }

    @PostMapping("/reviews/update")
    public String updateReviews(@ModelAttribute("reviewsChanges") ReviewsListWrapper reviewsChanges) {
        reviewService.updateHiddenFields(reviewsChanges);
        return "redirect:/reviews";
    }

    @GetMapping("/reviews/new")
    public String createReview() {
        return "newReview";
    }

    @PostMapping("/reviews/new")
    public String createReview(Authentication authentication,
                               @ModelAttribute("content") String content) {
        Long userId = userAuthenticationService.getSecureUserId(authentication);
        reviewService.create(userId, content);
        return "redirect:/articles";
    }


}
