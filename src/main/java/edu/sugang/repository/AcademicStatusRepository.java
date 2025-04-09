package edu.sugang.repository;

import edu.sugang.domain.AcademicStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface AcademicStatusRepository extends JpaRepository<AcademicStatus, Integer> {

    List<AcademicStatus> findByStudentId(Integer studentId);

}
