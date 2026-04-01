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

    // Lien avec le patient (optionnel pour les séances anonymes)
    @ManyToOne(optional = true)
    @JoinColumn(name = "patient_id", nullable = true)
    private Patient patient;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDateTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDateTime dateFin) {
        this.dateFin = dateFin;
    }

    public Integer getDureeMinutes() {
        return dureeMinutes;
    }

    public void setDureeMinutes(Integer dureeMinutes) {
        this.dureeMinutes = dureeMinutes;
    }

    public Double getWattsMin() {
        return wattsMin;
    }

    public void setWattsMin(Double wattsMin) {
        this.wattsMin = wattsMin;
    }

    public Double getWattsMoy() {
        return wattsMoy;
    }

    public void setWattsMoy(Double wattsMoy) {
        this.wattsMoy = wattsMoy;
    }

    public Double getWattsMax() {
        return wattsMax;
    }

    public void setWattsMax(Double wattsMax) {
        this.wattsMax = wattsMax;
    }

    public Integer getBpmMin() {
        return bpmMin;
    }

    public void setBpmMin(Integer bpmMin) {
        this.bpmMin = bpmMin;
    }

    public Integer getBpmMoy() {
        return bpmMoy;
    }

    public void setBpmMoy(Integer bpmMoy) {
        this.bpmMoy = bpmMoy;
    }

    public Integer getBpmMax() {
        return bpmMax;
    }

    public void setBpmMax(Integer bpmMax) {
        this.bpmMax = bpmMax;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}