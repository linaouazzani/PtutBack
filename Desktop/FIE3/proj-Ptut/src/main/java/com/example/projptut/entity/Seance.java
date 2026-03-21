package com.example.projptut.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "seances")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Seance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private Integer dureeMinutes;

    // Données biofeedback
    private Double wattsMin;
    private Double wattsMoy;
    private Double wattsMax;
    private Integer bpmMin;
    private Integer bpmMoy;
    private Integer bpmMax;

    // Mode de jeu
    private String mode; // "SUIVI", "LIBRE", "DEMO"

    // Lien avec le patient
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
}