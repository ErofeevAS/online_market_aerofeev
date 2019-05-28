package com.gmail.erofeev.st.alexei.onlinemarket.repository;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Article;

import java.util.List;

public interface ArticleRepository extends GenericRepository<Long, Article> {
    Integer getAmountOfArticlesWithKeyWord(String keyWord);

    Integer getAmountOfArticlesWithSameTag(String tagName);

    List<Article> getArticlesFilteredByKeyWord(int offset, Integer amount, String keyWord);

    List<Article> getEntitiesByTag(int offset, Integer amount, String tag);

    List<Article> getEntitiesByTagAndKeyword(int offset, Integer amount, String tagId, String keyWord);
}
