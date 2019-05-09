package com.gmail.erofeev.st.alexei.onlinemarket.service.converter;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Comment;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.CommentDTO;

import java.util.List;

public interface CommentConverter {
    CommentDTO toDTO(Comment comment);

    Comment fromDTO(CommentDTO commentDTO);

    List<CommentDTO> toListDTO(List<Comment> comments);
}
