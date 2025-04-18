package com.project.Therapeutic_Connection_Platform.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "languages")
@JsonIgnoreProperties("therapists")
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lang_name", nullable = false, unique = true)
    private String langName;

    @Column(name = "lang_code", nullable = false, unique = true)
    private String langCode;


    @ManyToMany(mappedBy = "languages")
    private List<Therapist> therapists;


    public Language() {}
    public Language(String langName, String langCode) {
        this.langName = langName;
        this.langCode = langCode;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLangName() { return langName; }
    public void setLangName(String langName) { this.langName = langName; }

    public String getLangCode() { return langCode; }
    public void setLangCode(String langCode) { this.langCode = langCode; }

    public List<Therapist> getTherapists() { return therapists; }
    public void setTherapists(List<Therapist> therapists) { this.therapists = therapists; }
}
