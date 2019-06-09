package com.gmail.erofeev.st.alexei.onlinemarket.service.converter.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Profile;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.ProfileConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ProfileDTO;
import org.springframework.stereotype.Component;

@Component
public class ProfileConverterImpl implements ProfileConverter {
    @Override
    public ProfileDTO toDTO(Profile profile) {
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setId(profile.getId());
        profileDTO.setAddress(profile.getAddress());
        profileDTO.setPhone(profile.getPhone());
        return profileDTO;
    }

    @Override
    public Profile fromDTO(ProfileDTO profileDTO) {
        Profile profile = new Profile();
        profile.setAddress(profileDTO.getAddress());
        profile.setPhone(profileDTO.getPhone());
        return profile;
    }
}
