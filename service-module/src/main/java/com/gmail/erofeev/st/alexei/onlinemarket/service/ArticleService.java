package com.gmail.erofeev.st.alexei.onlinemarket.service;

import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ArticleDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ArticleRestDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.NewArticleDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.PageDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.SearchingFilter;

import java.util.List;

public interface ArticleService {
    PageDTO<ArticleDTO> getArticles(Integer page, Integer amountOfDisplayedEntities, SearchingFilter searchingFilter);

    ArticleDTO getArticleById(Long id);

    ArticleRestDTO getArticleByIdForRest(Long id);

    List<ArticleDTO> getArticles();

    List<ArticleRestDTO> getArticlesForRest(int offset, int amount);

    String deleteArticleById(Long id);

    void deleteArticleByIdForRest(Long id);

//    void add(ArticleDTO articleDTO);

//    Integer getAmountOfArticlesWithKeyWord(String keyWord);

//    PageDTO<ArticleDTO> getArticlesByTag(int page, int size, String tagName);

    void createArticle(NewArticleDTO article);

    void update(NewArticleDTO article);

    ArticleRestDTO addArticleForRest(ArticleRestDTO articleDTO);
}