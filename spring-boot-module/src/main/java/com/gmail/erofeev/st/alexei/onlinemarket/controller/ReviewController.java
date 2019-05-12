package com.gmail.erofeev.st.alexei.onlinemarket.controller;

import com.gmail.erofeev.st.alexei.onlinemarket.controller.util.PageSizeValidator;
import com.gmail.erofeev.st.alexei.onlinemarket.controller.util.Paginator;
import com.gmail.erofeev.st.alexei.onlinemarket.service.ReviewService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ReviewDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ReviewsListWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@Scope("session")
public class ReviewController {

    private final ReviewService reviewService;
    private final Paginator paginator;
    private final PageSizeValidator pageSizeValidator;

    @Autowired
    public ReviewController(ReviewService reviewService, Paginator paginator, PageSizeValidator pageSizeValidator) {
        this.reviewService = reviewService;
        this.paginator = paginator;
        this.pageSizeValidator = pageSizeValidator;
    }

    @GetMapping("/reviews")
    public String getReviews(Model model,
                             @RequestParam(defaultValue = "1", required = false) String page,
                             @RequestParam(defaultValue = "10", required = false) String size,
                             HttpServletRequest request) {
        int intPage = pageSizeValidator.validatePage(page);
        int intSize = pageSizeValidator.validateSize(size);
        Integer maxPage = reviewService.getAmount(intSize);
        paginator.validate(intPage, maxPage, intSize);
        model.addAttribute("paginator", paginator);
        List<ReviewDTO> reviews = reviewService.getReviews(paginator.getPage(), paginator.getSize());
        ReviewsListWrapper reviewsListWrapper = new ReviewsListWrapper(reviews);
        request.getSession().setAttribute("reviewsListWrapperOld", reviewsListWrapper);
        ReviewsListWrapper reviewsChanges = new ReviewsListWrapper(reviews);
        model.addAttribute("reviewsChanges", reviewsChanges);
        return "reviews";
    }

    @PostMapping("/reviews/{id}/delete")
    public String deleteReview(@PathVariable Long id) {
        reviewService.delete(id);
        return "redirect:/reviews";
    }

    @PostMapping("/reviews/update")
    public String updateReviews(@ModelAttribute("reviewsChanges") ReviewsListWrapper reviewsChanges,
                                HttpServletRequest request) {
        HttpSession session = request.getSession();
        ReviewsListWrapper reviewsListWrapper = (ReviewsListWrapper) session.getAttribute("reviewsListWrapperOld");
        reviewService.updateDifference(reviewsListWrapper, reviewsChanges);
        return "redirect:/reviews";
    }
}
