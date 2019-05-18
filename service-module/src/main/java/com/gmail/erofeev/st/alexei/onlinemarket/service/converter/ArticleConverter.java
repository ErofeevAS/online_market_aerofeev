package com.gmail.erofeev.st.alexei.onlinemarket.service.converter;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Article;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ArticleDTO;

import java.util.List;

public interface ArticleConverter {
    List<ArticleDTO> toListDTO(List<Article> articles);

    ArticleDTO toDTO(Article article);
}
