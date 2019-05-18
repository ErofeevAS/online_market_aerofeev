package com.gmail.erofeev.st.alexei.onlinemarket.service;

import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ArticleDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.PageDTO;

import java.util.List;

public interface ArticleService {
    PageDTO<ArticleDTO> getArticles(Integer page, Integer amount);

    ArticleDTO getArticleById(Long id);

    ArticleDTO getArticleByIdHiber(Long id);

    List<ArticleDTO> getAll();
}
