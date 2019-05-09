package com.gmail.erofeev.st.alexei.onlinemarket.repository.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.ArticleRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.exception.RepositoryException;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Article;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Comment;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.User;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ArticleRepositoryImpl extends GenericRepositoryImpl implements ArticleRepository {
    private static final Logger logger = LoggerFactory.getLogger(ArticleRepositoryImpl.class);

    @Override
    public List<Article> getArticles(Connection connection, int offset, int amount) {
        List<Article> articles = new ArrayList<>();
        String sql = "SELECT articles.id as art_id,title,content,date,articles.deleted as art_deleted,hided,user_id,lastname,firstname,patronymic" +
                " FROM articles" +
                "  JOIN users on articles.user_id= users.id LIMIT ?,?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, offset);
            preparedStatement.setInt(2, amount);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Article article = getArticles(resultSet);
                articles.add(article);
            }
            return articles;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RepositoryException(e.getMessage(), e);
        }
    }

    private Article getArticles(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("art_id");
        String title = resultSet.getString("title");
        Timestamp date = resultSet.getTimestamp("date");
        String content = resultSet.getString("content");
        Boolean art_deleted = resultSet.getBoolean("art_deleted");
        Boolean hided = resultSet.getBoolean("hided");
        long user_id = resultSet.getLong("user_id");
        String lastName = resultSet.getString("lastname");
        String firstName = resultSet.getString("firstname");
        String patronymic = resultSet.getString("patronymic");
        User user = new User(user_id, lastName, firstName, patronymic);
        Article article = new Article(id, title, user, content, date, art_deleted, hided, new ArrayList<Comment>());
        return article;
    }
}
