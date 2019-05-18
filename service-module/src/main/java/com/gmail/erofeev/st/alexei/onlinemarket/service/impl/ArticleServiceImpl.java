package com.gmail.erofeev.st.alexei.onlinemarket.service.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.ArticleRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Article;
import com.gmail.erofeev.st.alexei.onlinemarket.service.ArticleService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.ArticleConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ArticleDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.PageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
    private static final Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);
    private final ArticleRepository articleRepository;
    private final ArticleConverter articleConverter;

    public ArticleServiceImpl(ArticleRepository articleRepository, ArticleConverter articleConverter) {
        this.articleRepository = articleRepository;
        this.articleConverter = articleConverter;
    }

    @Override
    @Transactional
    public PageDTO<ArticleDTO> getArticles(Integer page, Integer amount) {
        Long amountOfArticles = articleRepository.getAmountOfEntity();
        List<Article> articles = articleRepository.getEntities(page, amount);
        for (Article article : articles) {
            article.getUser();
            article.getComments();
        }
        List<ArticleDTO> articleDTOList = articleConverter.toListDTO(articles);
        PageDTO<ArticleDTO> pageDTO = new PageDTO<>();
//        pageDTO.setAmountOfPages(amountOfArticles);
//        pageDTO.setList(articleDTOList);
        return pageDTO;
    }

    @Override
    public ArticleDTO getArticleById(Long id) {
//        try (Connection connection = articleRepository.getConnection()) {
//            connection.setAutoCommit(false);
//            try {
//                Article article = articleRepository.findArticleById(connection, id);
//                ArticleDTO articleDTO = articleConverter.toDTO(article);
//                connection.commit();
//                return articleDTO;
//            } catch (SQLException e) {
//                connection.rollback();
//                logger.error(e.getMessage(), e);
//                throw new ServiceException(String.format("Can't get article with id: %s", id), e);
//            }
//        } catch (SQLException e) {
//            logger.error(e.getMessage(), e);
//            throw new ServiceException("Can't establish connection to database.", e);
//        }
        return null;
    }

    @Override
    @Transactional
    public ArticleDTO getArticleByIdHiber(Long id) {
        Article article = articleRepository.findById(id);
        return articleConverter.toDTO(article);
    }

    @Override
    @Transactional
    public List<ArticleDTO> getAll() {
        List<Article> articles = articleRepository.findAll();
        return articleConverter.toListDTO(articles);
    }

    private int getOffset(int page, int amount) {
        return (page - 1) * amount;
    }

    private Integer getAmountOfPages(int amountOfDisplayedArticles, int amountOfArticles) {
        amountOfDisplayedArticles = Math.abs(amountOfDisplayedArticles);
        return (Math.round(amountOfArticles / amountOfDisplayedArticles) + 1);
    }
}
