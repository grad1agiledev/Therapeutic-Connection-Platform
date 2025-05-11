package com.project.Therapeutic_Connection_Platform.dto;

import java.util.List;

public class TherapistUpdateRequest {

    public List<String> specializations;
    public String bio;
    public Double sessionCost;

    public List<String> languages;
    public Long locationId;
    public String profilePicture;
    public List<Long> languageIds;
    public Boolean isVirtual;
}
