package com.gmail.erofeev.st.alexei.onlinemarket.repository.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.ArticleRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.exception.RepositoryException;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Article;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.User;
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
public class ArticleRepositoryImpl extends GenericRepositoryHiberImpl<Long, Article> implements ArticleRepository {
    private static final Logger logger = LoggerFactory.getLogger(ArticleRepositoryImpl.class);

    public List<Article> getArticles(Connection connection, int offset, int amount) {
        List<Article> articles = new ArrayList<>();
        String sql = "SELECT a.id ,a.title,a.content,a.date,a.deleted,a.hided,a.user_id,u.lastname,u.firstname" +
                " FROM article as a" +
                " JOIN users as u ON u.id = a.user_id  where a.hided = false ORDER BY a.title LIMIT ?,?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, offset);
            preparedStatement.setInt(2, amount);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Article article = getArticle(resultSet);
                articles.add(article);
            }
            return articles;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RepositoryException("Can't get articles", e);
        }
    }

    public Article findArticleById(Connection connection, Long id) {
        String sql =
                "SELECT a.id,a.title AS art_title,a.content AS art_content,a.date AS art_date" +
                        ", u.lastname AS art_user_lastname,u.firstname AS art_user_firstname" +
                        ", c.id as c_id,c.user_id AS comm_user_id,c.date AS c_date,c.content AS c_content,c.comment_parent_id AS c_parent_id" +
                        " FROM articles AS a" +
                        " JOIN users AS u ON a.user_id = u.id" +
                        " JOIN articles_comments AS a_c ON a.id = a_c.article_id" +
                        " JOIN comments AS c ON a_c.comment_id=c.id WHERE a.id =?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return getArticle(resultSet);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RepositoryException(String.format("Can't get article with id: %s :", id + e.getMessage()), e);
        }
    }

    private Article getArticle(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String title = resultSet.getString("title");
        String content = resultSet.getString("content");
        Timestamp date = resultSet.getTimestamp("date");
        boolean hided = resultSet.getBoolean("hided");
        Article article = new Article();
        article.setId(id);
        article.setTitle(title);
        article.setContent(content);
        article.setDate(date);
        article.setHided(hided);
        Long userId = resultSet.getLong("user_id");
        String lastName = resultSet.getString("lastname");
        String firstName = resultSet.getString("firstname");
        User user = new User();
        user.setId(userId);
        user.setLastName(lastName);
        user.setFirstName(firstName);
//        article.setUser(user);
        return article;
    }

//    private Article getFullArticle(ResultSet resultSet) throws SQLException {
//        Map<Long, List<Long>> parentChildren = new HashMap<>();
//        Map<Long, Comment> comments = new HashMap<>();
//        Article article = new Article();
//
//        while (resultSet.next()) {
//            long id = resultSet.getLong("id");
//            String articleTitle = resultSet.getString("art_title");
//            String art_content = resultSet.getString("art_content");
//            Timestamp art_date = resultSet.getTimestamp("art_date");
//
//            article.setId(id);
//            article.setTitle(articleTitle);
//            article.setContent(art_content);
//            article.setDate(art_date);
//            long art_user_id = resultSet.getLong("art_user_id");
//            String articleUserLastName = resultSet.getString("art_user_lastname");
//            String articleUserFirstName = resultSet.getString("art_user_firstname");
//            User articleUser = new User();
//            articleUser.setId(art_user_id);
//            articleUser.setLastName(articleUserLastName);
//            articleUser.setFirstName(articleUserFirstName);
//            article.setUser(articleUser);
//
//            Long commentId = resultSet.getLong("c_id");
//            Long commentParentId = resultSet.getLong("c_parent_id");
//            String commentContent = resultSet.getString("c_content");
//            Timestamp commentDate = resultSet.getTimestamp("date");
//            String commLastName = resultSet.getString("comm_lastname");
//            String commFirstName = resultSet.getString("comm_firstname");
//            long userId = resultSet.getLong("comm_user_id");
//            User user = new User();
//            user.setId(userId);
//            user.setFirstName(commFirstName);
//            user.setLastName(commLastName);
//
//            Comment comment = new Comment();
//            comment.setId(commentId);
//            comment.setContent(commentContent);
//            comment.setDate(commentDate);
//            comment.setUser(user);
//            comments.put(commentParentId, comment);
//            if (!parentChildren.containsKey(commentParentId)) {
//                List<Long> children = new ArrayList<>();
//                children.add(commentId);
//                parentChildren.put(commentParentId, children);
//            } else {
//                parentChildren.get(commentParentId).add(commentId);
//            }
//        }
//
//
//        for (Map.Entry<Long, List<Long>> entry : parentChildren.entrySet()) {
//            Comment comment1 = comments.get(entry.getKey());
//            List<Long> childrenId = entry.getValue();
//            for (Long id : childrenId) {
//                Comment commentChild = comments.get(id);
//                List<Comment> children = comment1.getChildren();
//                children.add(commentChild);
//            }
//            Comment rootComment = comments.get(0);
//
//        }
//
//        return null;
//    }

}
