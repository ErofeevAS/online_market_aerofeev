package com.gmail.erofeev.st.alexei.onlinemarket.service;

import com.gmail.erofeev.st.alexei.onlinemarket.service.model.CommentDTO;

public interface CommentService {
    void save(Long userId, CommentDTO commentDTO);

    void deleteComment(Long id);
}