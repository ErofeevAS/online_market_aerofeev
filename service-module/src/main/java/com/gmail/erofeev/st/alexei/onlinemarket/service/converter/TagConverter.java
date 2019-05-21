package com.gmail.erofeev.st.alexei.onlinemarket.service.converter;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Tag;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.TagDTO;

import java.util.List;

public interface TagConverter {
    TagDTO toDTO(Tag tag);

    List<TagDTO> toListDTO(List<Tag> tag);
}
