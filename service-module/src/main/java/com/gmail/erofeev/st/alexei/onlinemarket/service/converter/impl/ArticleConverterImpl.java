package com.gmail.erofeev.st.alexei.onlinemarket.service.converter.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Article;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Comment;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.ArticleConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.CommentConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.UserConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ArticleDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.CommentDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class ArticleConverterImpl implements ArticleConverter {
    private final UserConverter userConverter;
    private final CommentConverter commentConverter;

    @Autowired
    public ArticleConverterImpl(UserConverter userConverter, CommentConverter commentConverter) {
        this.userConverter = userConverter;
        this.commentConverter = commentConverter;
    }

    @Override
    public ArticleDTO toDTO(Article article) {
        Long id = article.getId();
        UserDTO user = userConverter.toDTO(article.getUser());
        String content = article.getContent();
        Timestamp date = article.getDate();
        List<Comment> comments = article.getComments();
        List<CommentDTO> commentDTOList = commentConverter.toListDTO(comments);
        Boolean deleted = article.getDeleted();
        Boolean hided = article.getHided();
        String title = article.getTitle();
        ArticleDTO articleDTO = new ArticleDTO(id, title, user, content, date, deleted, hided, commentDTOList);
        return articleDTO;
    }

    @Override
    public Article fromDTO(ArticleDTO articleDTO) {
        Long id = articleDTO.getId();
        Boolean deleted = articleDTO.getDeleted();
        Boolean hided = articleDTO.getHided();
        Article article = new Article(id, deleted, hided);
        return article;
    }

    @Override
    public List<ArticleDTO> toListDTO(List<Article> articles) {
        List<ArticleDTO> articleDTOList = new ArrayList<>();
        for (Article article : articles) {
            articleDTOList.add(toDTO(article));
        }
        return articleDTOList;
    }
}
