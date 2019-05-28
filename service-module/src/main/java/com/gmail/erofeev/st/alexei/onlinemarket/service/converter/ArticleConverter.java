package com.gmail.erofeev.st.alexei.onlinemarket.service.converter;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Article;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ArticleDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ArticleRestDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.NewArticleDTO;

import java.util.List;

public interface ArticleConverter {
    List<ArticleDTO> toListDTO(List<Article> articles);

    ArticleDTO toDTO(Article article);

    ArticleRestDTO toRestDTO(Article article);

    List<ArticleRestDTO> toListRestDTO(List<Article> articles);

    Article fromRestDTO(ArticleRestDTO articleRestDTO);

    Article fromDTO(ArticleDTO articleDTO);

    Article toArticle(NewArticleDTO newArticleDTO);
}