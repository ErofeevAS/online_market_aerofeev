package com.gmail.erofeev.st.alexei.onlinemarket.repository.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.ArticleRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.exception.RepositoryException;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Article;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ArticleRepositoryImpl extends GenericRepositoryImpl<Long, Article> implements ArticleRepository {
    private static final Logger logger = LoggerFactory.getLogger(ArticleRepositoryImpl.class);

    @Override
    public Integer getAmountOfArticlesWithKeyWord(String keyWord) {
        String query = "select count(a) from " + entityClass.getName() + " a where a.title like :keyWord";
        Query q = entityManager.createQuery(query);
        q.setParameter("keyWord", "%" + keyWord + "%");
        return ((Number) q.getSingleResult()).intValue();
    }

    @Override
    public List<Article> getArticlesFilteredByTitle(int offset, Integer amount, String keyWord) {
        String query = "select a from " + entityClass.getName() + " a where a.title like :keyWord  ORDER BY a.date DESC";
        Query q = entityManager.createQuery(query);
        q.setParameter("keyWord", "%" + keyWord + "%");
        q.setFirstResult(offset);
        q.setMaxResults(amount);
        return q.getResultList();
    }

    @Override
    public Integer getAmountOfArticlesWithSameTag(String tag) {
        String query = "select count(a) from " + entityClass.getName() + " a where a.tags.name= :tag";
        Query q = entityManager.createQuery(query);
        q.setParameter("tag", tag);
        return ((Number) q.getSingleResult()).intValue();
    }

    @Override
    public List<Article> getEntitiesByTag(int offset, Integer amount, String tagId) {
        String query = "select a from " + entityClass.getName() + " a  JOIN a.tags t WHERE t.id = :tagId ORDER BY a.date DESC";
        Query q = entityManager.createQuery(query);
        q.setParameter("tagId", Long.parseLong(tagId));
        q.setFirstResult(offset);
        q.setMaxResults(amount);
        return q.getResultList();
    }
}
