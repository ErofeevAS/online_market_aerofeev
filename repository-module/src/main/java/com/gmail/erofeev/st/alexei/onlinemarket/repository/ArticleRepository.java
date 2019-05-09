package com.gmail.erofeev.st.alexei.onlinemarket.repository;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Article;

import java.sql.Connection;
import java.util.List;

public interface ArticleRepository extends GenericRepository {
    List<Article> getArticles(Connection connection, int offset, int amount);
}
