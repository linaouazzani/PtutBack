package com.example.projptut.repository;

import com.example.projptut.entity.Seance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SeanceRepository extends JpaRepository<Seance, Long> {
    List<Seance> findByPatientId(Long patientId);
    List<Seance> findByPatientIsNull();
}