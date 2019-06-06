package com.gmail.erofeev.st.alexei.onlinemarket.service.converter.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Article;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Comment;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Tag;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.User;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.ArticleConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.CommentConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.TagConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.UserConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ArticleDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ArticleRestDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.CommentDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.NewArticleDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.TagDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.UserDTO;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ArticleConverterImpl implements ArticleConverter {
    private final UserConverter userConverter;
    private final CommentConverter commentConverter;
    private final TagConverter tagConverter;
    private final DateTimeConverterImpl dateTimeConverter;

    public ArticleConverterImpl(UserConverter userConverter,
                                CommentConverter commentConverter,
                                TagConverter tagConverter,
                                DateTimeConverterImpl dateTimeConverter) {
        this.userConverter = userConverter;
        this.commentConverter = commentConverter;
        this.tagConverter = tagConverter;
        this.dateTimeConverter = dateTimeConverter;
    }

    @Override
    public List<ArticleDTO> toListDTO(List<Article> articles) {
        return articles.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ArticleDTO toDTO(Article article) {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setId(article.getId());
        articleDTO.setTitle(article.getTitle());
        String date = article.getCreatedDate().toLocalDateTime().toString();
        articleDTO.setCreatedDate(date);
        articleDTO.setContent(article.getContent());
        List<Comment> comments = article.getComments();
        List<CommentDTO> commentDTOList = commentConverter.toListDTO(comments);
        articleDTO.setComments(commentDTOList);
        UserDTO userDTO = userConverter.toDTO(article.getUser());
        userDTO.setPassword("");
        articleDTO.setUser(userDTO);
        List<Tag> tags = article.getTags();
        List<TagDTO> tagsDTO = tagConverter.toListDTO(tags);
        articleDTO.setTags(tagsDTO);
        return articleDTO;
    }

    @Override
    public ArticleRestDTO toRestDTO(Article article) {
        ArticleRestDTO articleRestDTO = new ArticleRestDTO();
        articleRestDTO.setId(article.getId());
        articleRestDTO.setTitle(article.getTitle());
        articleRestDTO.setContent(article.getContent());
        articleRestDTO.setCreatedDate(article.getCreatedDate());
        User user = article.getUser();
        articleRestDTO.setAuthorId(user.getId());
        articleRestDTO.setAuthorFirstName(user.getFirstName());
        articleRestDTO.setAuthorLastName(user.getLastName());
        return articleRestDTO;
    }

    @Override
    public List<ArticleRestDTO> toListRestDTO(List<Article> articles) {
        return articles.stream()
                .map(this::toRestDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Article fromRestDTO(ArticleRestDTO articleRestDTO) {
        Article article = new Article();
        article.setTitle(articleRestDTO.getTitle());
        article.setContent(articleRestDTO.getContent());
        article.setCreatedDate(articleRestDTO.getCreatedDate());
        User user = new User();
        user.setId(articleRestDTO.getAuthorId());
        article.setUser(user);
        return article;
    }

    @Override
    public Article fromDTO(ArticleDTO articleDTO) {
        Article article = new Article();
        article.setTitle(articleDTO.getTitle());
        article.setContent(articleDTO.getContent());
        String dateFromPage = articleDTO.getCreatedDate();
        Timestamp date = dateTimeConverter.convertDateTimeLocaleToTimeStamp(dateFromPage);
        article.setCreatedDate(date);
        return article;
    }

    @Override
    public Article toArticle(NewArticleDTO newArticleDTO) {
        Article article = new Article();
        article.setContent(newArticleDTO.getContent());
        article.setTitle(newArticleDTO.getTitle());
        String dateFromPage = newArticleDTO.getCreatedDate();
        Timestamp date = dateTimeConverter.convertDateTimeLocaleToTimeStamp(dateFromPage);
        article.setCreatedDate(date);
        return article;
    }
}
