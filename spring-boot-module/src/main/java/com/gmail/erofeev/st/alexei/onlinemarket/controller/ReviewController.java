package com.gmail.erofeev.st.alexei.onlinemarket.controller;

import com.gmail.erofeev.st.alexei.onlinemarket.controller.util.Paginator;
import com.gmail.erofeev.st.alexei.onlinemarket.service.ReviewService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ReviewDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ReviewHideFieldState;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ReviewsHidedFieldChanges;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ReviewController {

    private final ReviewService reviewService;
    private List<ReviewHideFieldState> tempReviewHideFieldStates = new ArrayList<>();

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/reviews")
    public String getReviews(Model model,
                             @RequestParam(defaultValue = "1", required = false) int page,
                             @RequestParam(defaultValue = "10", required = false) int size) {
        Integer maxPage = reviewService.getAmount(size);
        Paginator paginator = new Paginator(page, maxPage, size);
        List<ReviewDTO> reviews = reviewService.getReviews(page, size);
        tempReviewHideFieldStates = reviewService.getIdAndHidedState(reviews);
        model.addAttribute("paginator", paginator);
        ReviewsHidedFieldChanges reviewsChanges = new ReviewsHidedFieldChanges();
        reviewsChanges.setReviews(reviews);
        model.addAttribute("reviewsChanges", reviewsChanges);
        return "reviews";
    }

    @PostMapping("/reviews/update")
    public String updateReviews(@ModelAttribute("reviewsChanges") ReviewsHidedFieldChanges reviewsHidedFieldChanges) {
        List<ReviewHideFieldState> newReviewHideFieldStates = reviewService.getIdAndHidedState(reviewsHidedFieldChanges.getReviews());
        reviewService.updateHidedFields(tempReviewHideFieldStates, newReviewHideFieldStates);
        return "redirect:/reviews";
    }

    @PostMapping("/reviews/{id}/delete")
    public String deleteReview(@PathVariable Long id) {
        reviewService.delete(id);
        return "redirect:/reviews";
    }
}
