package com.example.projptut.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class Patient extends Utilisateur {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "professionnel_id")
    @JsonIgnoreProperties({"motDePasse", "patients"})
    private ProfessionnelDeSante professionnelDeSante;

    public ProfessionnelDeSante getProfessionnelDeSante() {
        return professionnelDeSante;
    }

    public void setProfessionnelDeSante(ProfessionnelDeSante professionnelDeSante) {
        this.professionnelDeSante = professionnelDeSante;
    }

    public void prendreRendezVous() {
        System.out.println(getNom() + " prend un rendez-vous.");
    }
}
