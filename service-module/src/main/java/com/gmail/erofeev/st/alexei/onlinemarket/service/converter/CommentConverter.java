package com.gmail.erofeev.st.alexei.onlinemarket.service.converter;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Comment;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.CommentDTO;

import java.util.List;

public interface CommentConverter {
    List<CommentDTO> toListDTO(List<Comment> comments);

    CommentDTO toDTO(Comment comment);
}
