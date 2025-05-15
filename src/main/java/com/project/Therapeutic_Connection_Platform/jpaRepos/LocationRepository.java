package com.project.Therapeutic_Connection_Platform.jpaRepos;

import com.project.Therapeutic_Connection_Platform.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location,Long> {

    boolean existsByNameAndCountry(String name, String country);

}
