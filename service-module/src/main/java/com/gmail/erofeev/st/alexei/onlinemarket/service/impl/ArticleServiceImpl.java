package com.gmail.erofeev.st.alexei.onlinemarket.service.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.ArticleRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.UserRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Article;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.User;
import com.gmail.erofeev.st.alexei.onlinemarket.service.ArticleService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.ArticleConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.DateTimeConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ArticleDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ArticleRestDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.NewArticleDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.PageDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.RestEntityNotFoundException;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.SearchingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;

@Service
public class ArticleServiceImpl extends AbstractService<ArticleDTO> implements ArticleService {
    private static final Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);
    private static final String SUCCESSFUL_DELETE_MESSAGE = "success";
    private final ArticleRepository articleRepository;
    private final ArticleConverter articleConverter;
    private final UserRepository userRepository;
    private final DateTimeConverter dateTimeConverter;

    public ArticleServiceImpl(ArticleRepository articleRepository,
                              ArticleConverter articleConverter,
                              UserRepository userRepository,
                              DateTimeConverter dateTimeConverter) {
        this.articleRepository = articleRepository;
        this.articleConverter = articleConverter;
        this.userRepository = userRepository;
        this.dateTimeConverter = dateTimeConverter;
    }

    @Override
    @Transactional
    public PageDTO<ArticleDTO> getArticles(Integer page, Integer amount, SearchingFilter searchingFilter) {
        String keyWord = searchingFilter.getKeyWord();
        String tag = searchingFilter.getTag();
        if (keyWord == null && tag == null) {
            int amountOfEntity = articleRepository.getAmountOfEntity(false);
            int maxPages = getMaxPages(amountOfEntity, amount);
            int offset = getOffset(page, maxPages, amount);
            List<Article> articles = articleRepository.getArticles(offset, amount);
            List<ArticleDTO> articleDTOList = articleConverter.toListDTO(articles);
            return getPageDTO(articleDTOList, maxPages);
        } else if (keyWord == null) {
            int amountOfEntity = articleRepository.getAmountOfEntity(false);
            int maxPages = getMaxPages(amountOfEntity, amount);
            int offset = getOffset(page, maxPages, amount);
            List<Article> articles = articleRepository.getEntitiesByTag(offset, amount, searchingFilter.getTag());
            List<ArticleDTO> articleDTOList = articleConverter.toListDTO(articles);
            return getPageDTO(articleDTOList, maxPages);
        } else if (tag == null) {
            int amountOfEntity = articleRepository.getAmountOfEntity(false);
            int maxPages = getMaxPages(amountOfEntity, amount);
            int offset = getOffset(page, maxPages, amount);
            List<Article> articles = articleRepository.getArticlesFilteredByKeyWord(offset, amount, keyWord);
            List<ArticleDTO> articleDTOList = articleConverter.toListDTO(articles);
            return getPageDTO(articleDTOList, maxPages);
        } else {
            Integer amountOfEntity = articleRepository.getAmountOfArticlesWithKeyWord(searchingFilter.getKeyWord());
            int maxPages = getMaxPages(amountOfEntity, amount);
            int offset = getOffset(page, maxPages, amount);
            List<Article> articles = articleRepository.getEntitiesByTagAndKeyword(offset, amount, tag, keyWord);
            List<ArticleDTO> articleDTOList = articleConverter.toListDTO(articles);
            return getPageDTO(articleDTOList, maxPages);
        }
    }

    @Override
    @Transactional
    public ArticleDTO getArticleById(Long id) {
        Article article = articleRepository.findById(id);
        isExist(article, id);
        return articleConverter.toDTO(article);
    }

    @Override
    @Transactional
    public ArticleRestDTO getArticleByIdForRest(Long id) {
        Article article = articleRepository.findById(id);
        isExistForRest(article, id);
        return articleConverter.toRestDTO(article);
    }

    @Override
    @Transactional
    public List<ArticleDTO> getArticles() {
        List<Article> articles = articleRepository.findAll();
        return articleConverter.toListDTO(articles);
    }

    @Override
    @Transactional
    public List<ArticleRestDTO> getArticlesForRest(int offset, int amount) {
        List<Article> articles = articleRepository.getEntities(offset, amount);
        return articleConverter.toListRestDTO(articles);
    }

    @Override
    @Transactional
    public String deleteArticleById(Long id) {
        Article article = articleRepository.findById(id);
        isExist(article, id);
        articleRepository.remove(article);
        return SUCCESSFUL_DELETE_MESSAGE;
    }

    @Override
    @Transactional
    public String deleteArticleByIdForRest(Long id) {
        Article article = articleRepository.findById(id);
        isExistForRest(article, id);
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
        List<Article> articles = articleRepository.getArticlesFilteredByKeyWord(offset, amount, tagName);
        List<ArticleDTO> articleDTOList = articleConverter.toListDTO(articles);
        PageDTO<ArticleDTO> pageDTO = new PageDTO<>();
        pageDTO.setList(articleDTOList);
        pageDTO.setAmountOfPages(maxPages);
        return pageDTO;
    }

    @Override
    @Transactional
    public void createArticle(NewArticleDTO articleDTO) {
        Long userId = articleDTO.getUserId();
        Article article = articleConverter.toArticle(articleDTO);
        User user = userRepository.findById(userId);
        article.setUser(user);
        user.addArticle(article);
        articleRepository.persist(article);
    }

    @Override
    @Transactional
    public void update(NewArticleDTO articleDTO) {
        Long id = articleDTO.getId();
        Article article = articleRepository.findById(id);
        String date = articleDTO.getCreatedDate();
        Timestamp timestamp = dateTimeConverter.convertDateTimeLocaleToTimeStamp(date);
        article.setCreatedDate(timestamp);
        String content = articleDTO.getContent();
        article.setContent(content);
        String title = articleDTO.getTitle();
        article.setTitle(title);
        articleRepository.merge(article);
    }

    private void isExist(Article article, Long id) {
        String defaultMessage = "Article with id:%s not found. ";
        if (article == null) {
            String message = String.format(defaultMessage, id);
            logger.error(message);
            throw new EntityNotFoundException(String.format(message, id));
        }
    }

    private void isExistForRest(Article article, Long id) {
        String defaultMessage = "Article with id:%s not found. ";
        if (article == null) {
            String message = String.format(defaultMessage, id);
            logger.error(message);
            throw new RestEntityNotFoundException(String.format(message, id));
        }
    }
}
