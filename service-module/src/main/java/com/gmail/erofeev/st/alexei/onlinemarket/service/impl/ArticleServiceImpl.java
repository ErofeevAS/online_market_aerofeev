package com.gmail.erofeev.st.alexei.onlinemarket.service.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.ArticleRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.UserRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Article;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.User;
import com.gmail.erofeev.st.alexei.onlinemarket.service.ArticleService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.ArticleConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ArticleDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ArticleRestDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.PageDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.SearchingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
    private static final Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);
    private static final String SUCCESSFUL_DELETE_MESSAGE = "success";
    private final ArticleRepository articleRepository;
    private final ArticleConverter articleConverter;
    private final UserRepository userRepository;

    public ArticleServiceImpl(ArticleRepository articleRepository, ArticleConverter articleConverter, UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.articleConverter = articleConverter;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public PageDTO<ArticleDTO> getArticles(Integer page, Integer amount, SearchingFilter searchingFilter) {
        if (searchingFilter.getTag() != null) {
            Integer amountOfEntity = articleRepository.getAmountOfEntity();
            int maxPages = getMaxPages(amountOfEntity, amount);
            int offset = getOffset(page, maxPages, amount);
            List<Article> articles = articleRepository.getEntitiesByTag(offset, amount, searchingFilter.getTag());
            List<ArticleDTO> articleDTOList = articleConverter.toListDTO(articles);
            return getPageDTO(articleDTOList, maxPages);
        }
        if (searchingFilter.getKeyWord() == null) {
            Integer amountOfEntity = articleRepository.getAmountOfEntity();
            int maxPages = getMaxPages(amountOfEntity, amount);
            int offset = getOffset(page, maxPages, amount);
            List<Article> articles = articleRepository.getEntities(offset, amount);
            List<ArticleDTO> articleDTOList = articleConverter.toListDTO(articles);
            return getPageDTO(articleDTOList, maxPages);
        } else {
            Integer amountOfEntity = articleRepository.getAmountOfArticlesWithKeyWord(searchingFilter.getKeyWord());
            int maxPages = getMaxPages(amountOfEntity, amount);
            int offset = getOffset(page, maxPages, amount);
            List<Article> articles = articleRepository.getArticlesFilteredByTitle(offset, amount, searchingFilter.getKeyWord());
            List<ArticleDTO> articleDTOList = articleConverter.toListDTO(articles);
            return getPageDTO(articleDTOList, maxPages);
        }
    }

    @Override
    @Transactional
    public ArticleDTO getArticleById(Long id) {
        Article article = articleRepository.findById(id);
        return articleConverter.toDTO(article);
    }

    @Override
    @Transactional
    public List<ArticleDTO> getArticles() {
        List<Article> articles = articleRepository.findAll();
        return articleConverter.toListDTO(articles);
    }

    @Override
    @Transactional
    public List<ArticleRestDTO> getRestArticles() {
        List<Article> articles = articleRepository.findAll();
        return articleConverter.toListRestDTO(articles);
    }

    @Override
    @Transactional
    public String delete(Long id) {
        Article article = articleRepository.findById(id);
        articleRepository.remove(article);
        return SUCCESSFUL_DELETE_MESSAGE;
    }

    @Override
    @Transactional
    public void add(ArticleDTO articleDTO) {
        Long userId = articleDTO.getUser().getId();
        User user = userRepository.findById(userId);
        Article article = articleConverter.fromDTO(articleDTO);
        article.setUser(user);
        articleRepository.persist(article);
    }

    @Override
    @Transactional
    public Integer getAmountOfArticlesWithKeyWord(String keyWord) {
        return articleRepository.getAmountOfArticlesWithKeyWord(keyWord);
    }

    @Override
    @Transactional
    public PageDTO<ArticleDTO> getArticlesByTag(int page, int amount, String tagName) {
        Integer amountOfEntity = articleRepository.getAmountOfArticlesWithSameTag(tagName);
        int maxPages = getMaxPages(amountOfEntity, amount);
        int offset = getOffset(page, maxPages, amount);
        List<Article> articles = articleRepository.getArticlesFilteredByTitle(offset, amount, tagName);
        List<ArticleDTO> articleDTOList = articleConverter.toListDTO(articles);
        PageDTO<ArticleDTO> pageDTO = new PageDTO<>();
        pageDTO.setList(articleDTOList);
        pageDTO.setAmountOfPages(maxPages);
        return pageDTO;
    }

    private int getOffset(int page, int maxPages, int amount) {
        if (page > maxPages) {
            page = maxPages;
        }
        return (page - 1) * amount;
    }

    private int getMaxPages(int amountOfEntity, int amountOfDisplayedEntities) {
        int maxPages = (Math.round(amountOfEntity / amountOfDisplayedEntities) + 1);
        return maxPages;
    }

    private PageDTO<ArticleDTO> getPageDTO(List<ArticleDTO> articleDTOList, Integer maxPages) {
        PageDTO<ArticleDTO> pageDTO = new PageDTO<>();
        pageDTO.setList(articleDTOList);
        pageDTO.setAmountOfPages(maxPages);
        return pageDTO;
    }
}
