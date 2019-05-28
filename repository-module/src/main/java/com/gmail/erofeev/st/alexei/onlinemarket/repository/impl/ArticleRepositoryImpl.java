package com.gmail.erofeev.st.alexei.onlinemarket.repository.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.ArticleRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Article;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class ArticleRepositoryImpl extends GenericRepositoryImpl<Long, Article> implements ArticleRepository {
    @Override
    public Integer getAmountOfArticlesWithKeyWord(String keyWord) {
        String hql = "select count(a) from Article a where a.title like :keyWord";
        Query query = entityManager.createQuery(hql);
        query.setParameter("keyWord", "%" + keyWord + "%");
        return ((Number) query.getSingleResult()).intValue();
    }

    @Override
    public Integer getAmountOfArticlesWithSameTag(String tag) {
        String hql = "select count(a) from Article a where a.tags.name= :tag";
        Query query = entityManager.createQuery(hql);
        query.setParameter("tag", tag);
        return ((Number) query.getSingleResult()).intValue();
    }

    @Override
    public List<Article> getArticlesFilteredByKeyWord(int offset, Integer amount, String keyWord) {
        String hql = "select a from Article a where a.title like :keyWord  ORDER BY a.date DESC";
        Query query = entityManager.createQuery(hql);
        query.setParameter("keyWord", "%" + keyWord + "%");
        query.setFirstResult(offset);
        query.setMaxResults(amount);
        return query.getResultList();
    }

    @Override
    public List<Article> getEntitiesByTag(int offset, Integer amount, String tagId) {
        String hql = "select a from Article a  JOIN a.tags t WHERE t.id = :tagId ORDER BY a.date DESC";
        Query query = entityManager.createQuery(hql);
        query.setParameter("tagId", Long.parseLong(tagId));
        query.setFirstResult(offset);
        query.setMaxResults(amount);
        return query.getResultList();
    }

    @Override
    public List<Article> getEntitiesByTagAndKeyword(int offset, Integer amount, String tagId, String keyWord) {
        String hql = "select a from Article a  JOIN a.tags t WHERE t.id = :tagId and a.title like :keyWord ORDER BY a.date DESC";
        Query query = entityManager.createQuery(hql);
        query.setParameter("tagId", Long.parseLong(tagId));
        query.setParameter("keyWord", "%" + keyWord + "%");
        query.setFirstResult(offset);
        query.setMaxResults(amount);
        return query.getResultList();
    }
}
