package com.gmail.erofeev.st.alexei.onlinemarket.service.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.ArticleRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Article;
import com.gmail.erofeev.st.alexei.onlinemarket.service.ArticleService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.ArticleConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.exception.ServiceException;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ArticleDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
    private static final Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);
    private final ArticleRepository articleRepository;
    private final ArticleConverter articleConverter;

    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository, ArticleConverter articleConverter) {
        this.articleRepository = articleRepository;
        this.articleConverter = articleConverter;
    }

    @Override
    public Integer getAmount(int amountOfDisplayedArticles) {
        try (Connection connection = articleRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                Integer amount = articleRepository.getAmount(connection,"articles");
                amount = (Math.round(amount / amountOfDisplayedArticles) + 1);
                connection.commit();
                return amount;
            } catch (SQLException e) {
                connection.rollback();
                String message = "Can't get amount of articles from repository: " + e.getMessage();
                logger.error(message, e);
                throw new ServiceException(message, e);
            }
        } catch (SQLException e) {
            String message = "Can't establish connection to database: " + e.getMessage();
            logger.error(message, e);
            throw new ServiceException(message, e);
        }
    }

    @Override
    public List<ArticleDTO> getArticles(int page, int amount) {
        try (Connection connection = articleRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                int offset = (page - 1) * amount;
                List<Article> articles = articleRepository.getArticles(connection, offset, amount);
                List<ArticleDTO> articleDTOList = articleConverter.toListDTO(articles);
                connection.commit();
                return articleDTOList;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ServiceException(e.getMessage(), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void hide(List<Long> articlesIdForHiding) {

    }
}
