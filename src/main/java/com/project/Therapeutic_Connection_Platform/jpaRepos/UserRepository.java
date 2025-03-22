package com.project.Therapeutic_Connection_Platform.jpaRepos;

import com.project.Therapeutic_Connection_Platform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

    User findByFirebaseUid(String firebaseUid);
}
