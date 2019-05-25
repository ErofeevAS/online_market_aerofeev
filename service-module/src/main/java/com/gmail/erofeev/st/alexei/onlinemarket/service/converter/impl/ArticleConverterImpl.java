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
        Long id = article.getId();
        String title = article.getTitle();
        String date = article.getDate().toLocalDateTime().toString();
        String content = article.getContent();
        User user = article.getUser();
        boolean deleted = article.isDeleted();
        boolean hided = article.isHidden();
        List<Comment> comments = article.getComments();
        List<CommentDTO> commentDTOList = commentConverter.toListDTO(comments);
        UserDTO userDTO = userConverter.toDTO(user);
        userDTO.setPassword("");
        ArticleDTO articleDTO = new ArticleDTO();
        List<Tag> tags = article.getTags();
        List<TagDTO> tagsDTO = tagConverter.toListDTO(tags);
        articleDTO.setTags(tagsDTO);
        articleDTO.setComments(commentDTOList);
        articleDTO.setId(id);
        articleDTO.setTitle(title);
        articleDTO.setDate(date);
        articleDTO.setContent(content);
        articleDTO.setDeleted(deleted);
        articleDTO.setHidden(hided);
        articleDTO.setUser(userDTO);
        return articleDTO;
    }

    @Override
    public ArticleRestDTO toRestDTO(Article article) {
        Long id = article.getId();
        String title = article.getTitle();
        String content = article.getContent();
        Timestamp date = article.getDate();
        User user = article.getUser();
        ArticleRestDTO articleRestDTO = new ArticleRestDTO();
        articleRestDTO.setId(id);
        articleRestDTO.setTitle(title);
        articleRestDTO.setContent(content);
        articleRestDTO.setDate(date);
        articleRestDTO.setAuthorEmail(user.getEmail());
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
        String title = articleRestDTO.getTitle();
        String content = articleRestDTO.getContent();
        Timestamp date = articleRestDTO.getDate();
        Article article = new Article();
        article.setTitle(title);
        article.setContent(content);
        article.setDate(date);
        return article;
    }

    @Override
    public Article fromDTO(ArticleDTO articleDTO) {
        String title = articleDTO.getTitle();
        String content = articleDTO.getContent();
        String dateFromPage = articleDTO.getDate();
        Timestamp date = dateTimeConverter.convertDateTimeLocaleToTimeStamp(dateFromPage);
        Article article = new Article();
        article.setTitle(title);
        article.setDate(date);
        article.setContent(content);
        return article;
    }

    @Override
    public Article toArticle(NewArticleDTO newArticleDTO) {
        String content = newArticleDTO.getContent();
        String title = newArticleDTO.getTitle();
        String dateFromPage = newArticleDTO.getDate();
        Timestamp date = dateTimeConverter.convertDateTimeLocaleToTimeStamp(dateFromPage);
        Article article = new Article();
        article.setDate(date);
        article.setContent(content);
        article.setTitle(title);
        return article;
    }

}
