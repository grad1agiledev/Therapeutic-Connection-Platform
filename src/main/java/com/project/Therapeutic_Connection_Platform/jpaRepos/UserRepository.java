package com.project.Therapeutic_Connection_Platform.jpaRepos;

import com.project.Therapeutic_Connection_Platform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    User findByFirebaseUid(String firebaseUid);
    List<User> findByRole(String role);
}
