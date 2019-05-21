package com.gmail.erofeev.st.alexei.onlinemarket.service.converter.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Article;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Comment;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.User;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.CommentConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.UserConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.CommentDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.UserDTO;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
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
        Long id = comment.getId();
        User user = comment.getUser();
        UserDTO userDTO = userConverter.toDTO(user);
        String content = comment.getContent();
        Timestamp date = comment.getDate();
        boolean hided = comment.isHidden();
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(id);
        commentDTO.setContent(content);
        commentDTO.setUser(userDTO);
        commentDTO.setDate(date);
        commentDTO.setHidden(hided);
        return commentDTO;
    }

    @Override
    public Comment fromDTO(CommentDTO commentDTO) {
        String content = commentDTO.getContent();
        Timestamp date = commentDTO.getDate();
        Long articleId = commentDTO.getArticleId();
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setDate(date);
        Article article = new Article();
        article.setId(articleId);
        comment.setArticle(article);
        return comment;
    }
}
