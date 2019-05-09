package com.gmail.erofeev.st.alexei.onlinemarket.service;

import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ArticleDTO;

import java.util.List;

public interface ArticleService {
    Integer getAmount(int amountOfDisplayedArticles);

    List<ArticleDTO> getArticles(int page, int amount);

    void hide(List<Long> articlesIdForHiding);

}
