package com.gmail.erofeev.st.alexei.onlinemarket.service.converter.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Tag;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.TagConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.TagDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TagConverterImpl implements TagConverter {
    @Override
    public TagDTO toDTO(Tag tag) {
        Long id = tag.getId();
        String name = tag.getName();
        TagDTO tagDTO = new TagDTO();
        tagDTO.setId(id);
        tagDTO.setName(name);
        return tagDTO;
    }

    @Override
    public List<TagDTO> toListDTO(List<Tag> tag) {
        return tag.stream().
                map(this::toDTO)
                .collect(Collectors.toList());
    }
}