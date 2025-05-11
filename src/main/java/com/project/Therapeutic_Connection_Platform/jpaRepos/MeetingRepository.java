package com.project.Therapeutic_Connection_Platform.jpaRepos;

import com.project.Therapeutic_Connection_Platform.model.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {

    Optional<Meeting> findById(Long id);

    List<Meeting> findAll();

    // Find meetings where a specific user ID is in the participantIds list
    @Query("SELECT m FROM Meeting m WHERE :userId MEMBER OF m.participantIds")
    List<Meeting> findByUserId(@Param("userId") String userId);

    // Find meetings happening after a certain date
    List<Meeting> findByStartTimeAfter(LocalDateTime startTime);

    // Find meetings happening before a certain date
    List<Meeting> findByEndTimeBefore(LocalDateTime endTime);

    // Find meetings happening within a specific time range
    List<Meeting> findByStartTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
}
