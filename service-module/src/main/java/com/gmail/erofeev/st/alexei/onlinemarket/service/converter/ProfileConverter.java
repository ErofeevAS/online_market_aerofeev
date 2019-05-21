package com.gmail.erofeev.st.alexei.onlinemarket.service.converter;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Profile;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ProfileDTO;

public interface ProfileConverter {
    ProfileDTO toDTO(Profile profile);

    Profile fromDTO(ProfileDTO profileDTO);
}