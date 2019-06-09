package com.gmail.erofeev.st.alexei.onlinemarket.service.converter.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Article;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Comment;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.CommentConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.UserConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.CommentDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.UserDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentConverterImpl implements CommentConverter {
    private final UserConverter userConverter;

    public CommentConverterImpl(UserConverter userConverter) {
        this.userConverter = userConverter;
    }

    @Override
    public List<CommentDTO> toListDTO(List<Comment> comments) {
        return comments.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public CommentDTO toDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setContent(comment.getContent());
        commentDTO.setCreatedDate(comment.getCreatedDate());
        UserDTO userDTO = userConverter.toDTO(comment.getUser());
        commentDTO.setUser(userDTO);
        return commentDTO;
    }

    @Override
    public Comment fromDTO(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());
        comment.setCreatedDate(commentDTO.getCreatedDate());
        Article article = new Article();
        article.setId(commentDTO.getArticleId());
        comment.setArticle(article);
        return comment;
    }
}
