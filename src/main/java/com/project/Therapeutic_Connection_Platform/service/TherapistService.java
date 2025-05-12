package com.project.Therapeutic_Connection_Platform.service;

import com.google.api.client.util.Value;
import com.project.Therapeutic_Connection_Platform.jpaRepos.TherapistRepository;
import com.project.Therapeutic_Connection_Platform.jpaRepos.UserRepository;
import com.project.Therapeutic_Connection_Platform.model.Therapist;
import com.project.Therapeutic_Connection_Platform.model.User;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TherapistService {

    private final TherapistRepository therapistRepository;
    @Autowired
    private UserRepository userRepository;
    // In a real application, this data would come from a database
    private final List<Therapist> therapists = new ArrayList<>();
   // private final RestTemplate restTemplate;


    @Value("${therapist.api.url}")
    private String therapistApiUrl;


    @PostConstruct
    public void init() {
        System.out.println("Therapist API URL: " + therapistApiUrl);
    }


    @Autowired
    public TherapistService(TherapistRepository therapistRepository) {
        this.therapistRepository = therapistRepository;
    }

//    public TherapistService(RestTemplate restTemplate)
//    {
//        this.restTemplate = restTemplate;
//    }

    public List<Therapist> getAllTherapists() {
        return therapistRepository.findAll();
    }


    public Therapist getTherapistByFirebaseUid(String firebaseUid) {
        User user = userRepository.findByFirebaseUid(firebaseUid);
        if (user == null || !"therapist".equalsIgnoreCase(user.getRole())) {
            return null;
        }
        return therapistRepository.findByUser(user);
    }

    public Therapist saveTherapist(Therapist therapist) {
        return therapistRepository.save(therapist);
    }

    public Therapist getTherapistById(String id) {
        try {
            Long therapistId = Long.parseLong(id);
            return therapistRepository.findById(therapistId).orElse(null);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public List<Therapist> searchTherapists(String specialization, String location, List<String> languages) {
        List<Therapist> allTherapists = therapistRepository.findAll();
        
        return allTherapists.stream()
            .filter(therapist -> {
                boolean matchesSpecialization = specialization == null || 
                    (therapist.getSpecialization() != null && 
                     therapist.getSpecialization().toLowerCase().contains(specialization.toLowerCase()));
                
                boolean matchesLocation = location == null || 
                    (therapist.getLocation() != null && 
                     therapist.getLocation().getName().toLowerCase().contains(location.toLowerCase()));
                
                boolean matchesLanguages = languages == null || languages.isEmpty() || 
                    (therapist.getLanguages() != null && 
                     therapist.getLanguages().stream()
                         .anyMatch(lang -> languages.stream()
                             .anyMatch(searchLang -> lang.getLangName().toLowerCase()
                                 .contains(searchLang.toLowerCase()))));
                
                return matchesSpecialization && matchesLocation && matchesLanguages;
            })
            .collect(Collectors.toList());
    }

} 