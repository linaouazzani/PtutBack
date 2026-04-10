package com.example.projptut.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class ProfessionnelDeSante extends Utilisateur {

    @OneToMany(mappedBy = "professionnelDeSante", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"professionnelDeSante", "motDePasse"})
    private List<Patient> patients;

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }

    public void consulterPatient(Patient patient) {
        System.out.println(getNom() + " consulte le patient " + patient.getNom());
    }
}
