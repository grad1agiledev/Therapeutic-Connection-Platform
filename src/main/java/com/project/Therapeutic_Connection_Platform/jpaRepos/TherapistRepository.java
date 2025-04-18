package com.project.Therapeutic_Connection_Platform.jpaRepos;

import com.project.Therapeutic_Connection_Platform.model.Therapist;
import com.project.Therapeutic_Connection_Platform.model.User;
import com.project.Therapeutic_Connection_Platform.modelEnums.VerificationState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TherapistRepository extends JpaRepository<Therapist,Long> {

    Optional<Therapist> findById(Long id);
    List<Therapist> findAll();
    Therapist findByUser(User user);

    List<Therapist> findByVerificationState(VerificationState pending);
}
