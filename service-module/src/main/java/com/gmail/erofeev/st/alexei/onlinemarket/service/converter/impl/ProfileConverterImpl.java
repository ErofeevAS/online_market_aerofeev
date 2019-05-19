package com.gmail.erofeev.st.alexei.onlinemarket.service.converter.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Profile;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.ProfileConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ProfileDTO;
import org.springframework.stereotype.Component;

@Component
public class ProfileConverterImpl implements ProfileConverter {
    @Override
    public ProfileDTO toDTO(Profile profile) {
        if (profile == null) {
            return new ProfileDTO();
        }
        Long id = profile.getId();
        String address = profile.getAddress();
        String phone = profile.getPhone();
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setId(id);
        profileDTO.setAddress(address);
        profileDTO.setPhone(phone);
        return profileDTO;
    }

    @Override
    public Profile fromDTO(ProfileDTO profileDTO) {
        if (profileDTO == null) {
            return new Profile();
        }
        Long id = profileDTO.getId();
        String address = profileDTO.getAddress();
        String phone = profileDTO.getPhone();
        Profile profile = new Profile();
        profile.setId(id);
        profile.setAddress(address);
        profile.setPhone(phone);
        return profile;
    }
}
