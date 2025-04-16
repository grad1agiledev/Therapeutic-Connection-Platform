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

//    public TherapistService() {
//        /*
//         Adding sample data
//         */
//        //initSampleData();
//    }

//    private void initSampleData() {
//        therapists.add(new Therapist(
//                "1",
//                "Dr. Ayşe Yılmaz",
//                "Anxiety, Depression",
//                "Istanbul",
//                Arrays.asList("Turkish", "English"),
//                4.8,
//                500.0,
//                "https://randomuser.me/api/portraits/women/1.jpg",
//                "Clinical psychologist with 10 years of experience. Specialized in anxiety and depression treatment.",
//                true
//        ));
//
//        therapists.add(new Therapist(
//                "2",
//                "Dr. Mehmet Kaya",
//                "Family Therapy, Couples Therapy",
//                "Ankara",
//                Arrays.asList("Turkish"),
//                4.5,
//                450.0,
//                "https://randomuser.me/api/portraits/men/2.jpg",
//                "Expert psychologist with 15 years of experience in family and couples therapy.",
//                true
//        ));
//
//        therapists.add(new Therapist(
//                "3",
//                "Dr. Zeynep Demir",
//                "Trauma, EMDR",
//                "Izmir",
//                Arrays.asList("Turkish", "German"),
//                4.9,
//                550.0,
//                "https://randomuser.me/api/portraits/women/3.jpg",
//                "Clinical psychologist specialized in trauma treatment and EMDR.",
//                true
//        ));
//
//        therapists.add(new Therapist(
//                "4",
//                "Dr. Ali Can",
//                "Child and Adolescent Psychology",
//                "Bursa",
//                Arrays.asList("Turkish", "English"),
//                4.7,
//                480.0,
//                "https://randomuser.me/api/portraits/men/4.jpg",
//                "Expert psychologist with 8 years of experience in child and adolescent psychology.",
//                true
//        ));
//
//        therapists.add(new Therapist(
//                "5",
//                "Dr. Selin Yıldız",
//                "Cognitive Behavioral Therapy",
//                "Antalya",
//                Arrays.asList("Turkish", "French"),
//                4.6,
//                520.0,
//                "https://randomuser.me/api/portraits/women/5.jpg",
//                "Clinical psychologist specialized in cognitive behavioral therapy.",
//                true
//        ));
//    }
//
//    public List<Therapist> getAllTherapists()
//    {
//        Therapist[] therapists = restTemplate.getForObject("http://localhost:8080/api/therapists",Therapist[].class);
//
//        return Arrays.asList(therapists);
//    }

//    public Therapist getTherapistById(String id) {
////        return therapists.stream()
////                .filter(therapist -> therapist.getId().equals(id))
////                .findFirst()
////                .orElse(null);
//
//        return  restTemplate.getForObject(therapistApiUrl+"/therapists/"+id,Therapist.class);
//
//    }

//    public List<Therapist> searchTherapists(String specialization, String location, List<String> languages)
//    {
//
//        String url = therapistApiUrl + "/therapists/search?specialization=" + specialization + "&location=" + location;
////        return therapists.stream()
////                .filter(therapist -> specialization == null || therapist.getSpecialization().toLowerCase().contains(specialization.toLowerCase()))
////                .filter(therapist -> location == null || therapist.getLocation().equalsIgnoreCase(location))
////                .filter(therapist -> languages == null || languages.isEmpty() ||
////                        therapist.getLanguages().stream().anyMatch(languages::contains))
////                .collect(Collectors.toList());
//
//        Therapist[] searchTherapistsList= restTemplate.getForObject(url, Therapist[].class);
//        return Arrays.asList(searchTherapistsList);
//    }

    public Therapist saveTherapist(Therapist therapist) {
        return therapistRepository.save(therapist);
    }


} 