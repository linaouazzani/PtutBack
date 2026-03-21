package com.example.projptut.entity;

import jakarta.persistence.Entity;


@Entity
public class Patient extends Utilisateur {

    public void prendreRendezVous() {
        System.out.println(getNom() + " prend un rendez-vous.");
    }
}