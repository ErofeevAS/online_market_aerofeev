package com.gmail.erofeev.st.alexei.onlinemarket.service.converter.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Article;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.User;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.ArticleConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.UserConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ArticleDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.UserDTO;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ArticleConverterImpl implements ArticleConverter {

    private final UserConverter userConverter;

    public ArticleConverterImpl(UserConverter userConverter) {
        this.userConverter = userConverter;
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
        Timestamp date = article.getDate();
        String content = article.getContent();
        User user = article.getUser();
        user.setPassword("");
        boolean deleted = article.isDeleted();
        boolean hided = article.isHided();
        UserDTO userDTO = userConverter.toDTO(user);
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setId(id);
        articleDTO.setTitle(title);
        articleDTO.setDate(date);
        articleDTO.setContent(content);
        articleDTO.setDeleted(deleted);
        articleDTO.setHided(hided);
        articleDTO.setUser(userDTO);
        return articleDTO;
    }
}
