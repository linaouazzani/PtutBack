package com.example.projptut.repository;

import com.example.projptut.entity.Patient;
import com.example.projptut.entity.ProfessionnelDeSante;
import com.example.projptut.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur,Long> {

    Optional<Utilisateur> findByEmail(String email);
    List<Utilisateur> findAll();

    /** Patients assignés à un professionnel donné. */
    @Query("SELECT p FROM Patient p WHERE p.professionnelDeSante = :pro")
    List<Patient> findPatientsByPro(@Param("pro") ProfessionnelDeSante pro);
}
