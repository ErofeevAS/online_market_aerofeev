package com.gmail.erofeev.st.alexei.onlinemarket.service.converter.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Comment;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.User;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.CommentConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.UserConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.CommentDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class CommentConverterImpl implements CommentConverter {
    private final UserConverter userConverter;

    @Autowired
    public CommentConverterImpl(UserConverter userConverter) {
        this.userConverter = userConverter;
    }

    @Override
    public CommentDTO toDTO(Comment comment) {
        Long id = comment.getId();
        UserDTO user = userConverter.toDTO(comment.getUser());
        String content = comment.getContent();
        Timestamp date = comment.getDate();
        CommentDTO commentDTO = new CommentDTO(id, user, date, content);
        return commentDTO;
    }

    @Override
    public Comment fromDTO(CommentDTO commentDTO) {
        Long id = commentDTO.getId();
        User user = userConverter.fromDTO(commentDTO.getUser());
        String content = commentDTO.getContent();
        Timestamp date = commentDTO.getDate();
        Comment comment = new Comment(id, user, date, content);
        return comment;
    }

    @Override
    public List<CommentDTO> toListDTO(List<Comment> comments) {
        List<CommentDTO> commentDTOList = new ArrayList<>(comments.size());
        for (Comment comment : comments) {
            commentDTOList.add(toDTO(comment));
        }
        return commentDTOList;
    }
}
