package com.gmail.erofeev.st.alexei.onlinemarket.repository;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Article;

import java.util.List;

public interface ArticleRepository extends GenericRepository<Long, Article> {
    int getAmountOfArticlesWithKeyWord(String keyWord);

    int getAmountOfArticlesWithSameTag(String tagName);

    int getAmountOfArticlesWithKeyWordAndTag(String keyWord, String tag);

    List<Article> getArticlesFilteredByKeyWord(int offset, int amount, String keyWord);

    List<Article> getEntitiesByTag(int offset, int amount, String tag);

    List<Article> getEntitiesByTagAndKeyword(int offset, int amount, String tagId, String keyWord);

    List<Article> getArticles(int offset, int amount);

}
