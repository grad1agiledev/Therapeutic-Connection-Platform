package com.project.Therapeutic_Connection_Platform.jpaRepos;

import com.project.Therapeutic_Connection_Platform.model.Therapist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TherapistRepository extends JpaRepository<Therapist,Long> {

    Optional<Therapist> findById(Long id);
}
