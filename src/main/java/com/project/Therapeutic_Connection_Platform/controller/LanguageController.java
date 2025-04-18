package com.project.Therapeutic_Connection_Platform.controller;

import com.project.Therapeutic_Connection_Platform.jpaRepos.LanguageRepository;
import com.project.Therapeutic_Connection_Platform.model.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/languages")
@CrossOrigin(origins="http://localhost:3000")

public class LanguageController {

    @Autowired
    private LanguageRepository repo;

    @GetMapping
    public List<Language> all() { return repo.findAll(); }
}
