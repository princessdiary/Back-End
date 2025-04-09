package edu.sugang.repository;

import edu.sugang.domain.CourseHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseHistoryRepository extends JpaRepository<CourseHistory, Integer> {
    Optional<CourseHistory> findBySemester(Integer semester);
}
