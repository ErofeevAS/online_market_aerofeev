package com.gmail.erofeev.st.alexei.onlinemarket.service.converter.impl;

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
        boolean hided = comment.isHided();
//        List<Comment> children = comment.getChildren();
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(id);
        commentDTO.setContent(content);
        commentDTO.setUser(userDTO);
        commentDTO.setDate(date);
        commentDTO.setHided(hided);

        return commentDTO;
    }
}
