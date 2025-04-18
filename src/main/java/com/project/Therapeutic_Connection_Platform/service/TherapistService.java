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


} 