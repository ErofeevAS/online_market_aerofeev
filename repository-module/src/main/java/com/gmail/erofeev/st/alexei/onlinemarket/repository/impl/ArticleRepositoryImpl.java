package com.gmail.erofeev.st.alexei.onlinemarket.repository.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.ArticleRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Article;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class ArticleRepositoryImpl extends GenericRepositoryImpl<Long, Article> implements ArticleRepository {
    @Override
    public int getAmountOfArticlesWithKeyWord(String keyWord) {
        String hql = "select count(a) from Article a where a.title like :keyWord  ORDER BY a.createdDate DESC";
        Query query = entityManager.createQuery(hql);
        query.setParameter("keyWord", "%" + keyWord + "%");
        return ((Number) query.getSingleResult()).intValue();
    }

    @Override
    public int getAmountOfArticlesWithSameTag(String tag) {
        String hql = "select count(a) from Article a where a.tags.name= :tag  ORDER BY a.createdDate DESC";
        Query query = entityManager.createQuery(hql);
        query.setParameter("tag", tag);
        return ((Number) query.getSingleResult()).intValue();
    }

    @Override
    public List<Article> getArticlesFilteredByKeyWord(int offset, int amount, String keyWord) {
        String hql = "select a from Article a where a.title like :keyWord  ORDER BY a.createdDate DESC";
        Query query = entityManager.createQuery(hql);
        query.setParameter("keyWord", "%" + keyWord + "%");
        query.setFirstResult(offset);
        query.setMaxResults(amount);
        return query.getResultList();
    }

    @Override
    public List<Article> getEntitiesByTag(int offset, int amount, String tagName) {
        String hql = "select a from Article a  JOIN a.tags t WHERE t.name = :tagName ORDER BY a.createdDate DESC";
        Query query = entityManager.createQuery(hql);
        query.setParameter("tagName", tagName);
        query.setFirstResult(offset);
        query.setMaxResults(amount);
        return query.getResultList();
    }

    @Override
    public List<Article> getEntitiesByTagAndKeyword(int offset, int amount, String tagName, String keyWord) {
        String hql = "select a from Article a  JOIN a.tags t WHERE t.name = :tagName and a.title like :keyWord ORDER BY a.createdDate DESC";
        Query query = entityManager.createQuery(hql);
        query.setParameter("tagName", tagName);
        query.setParameter("keyWord", "%" + keyWord + "%");
        query.setFirstResult(offset);
        query.setMaxResults(amount);
        return query.getResultList();
    }

    @Override
    public List<Article> getArticles(int offset, int amount) {
        String hql = "select a from Article a  ORDER BY a.createdDate DESC";
        Query query = entityManager.createQuery(hql);
        query.setFirstResult(offset);
        query.setMaxResults(amount);
        return query.getResultList();
    }
}
