package com.example.projptut.Rest;

import com.example.projptut.entity.Patient;
import com.example.projptut.entity.Seance;
import com.example.projptut.Service.SeanceService;
import com.example.projptut.repository.UtilisateurRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/seances")
@CrossOrigin
public class SeanceController {

    private final SeanceService service;
    private final UtilisateurRepository utilisateurRepository;

    public SeanceController(SeanceService service, UtilisateurRepository utilisateurRepository) {
        this.service = service;
        this.utilisateurRepository = utilisateurRepository;
    }

    // Enregistrer une séance (patient optionnel — null = séance anonyme)
    @PostMapping
    public ResponseEntity<?> enregistrerSeance(@RequestBody Seance seance) {
        seance.setId(null);
        if (seance.getPatient() != null && seance.getPatient().getId() != null) {
            Patient patient = (Patient) utilisateurRepository.findById(seance.getPatient().getId())
                    .orElse(null);
            if (patient == null) {
                return ResponseEntity.badRequest().body("Patient introuvable avec id=" + seance.getPatient().getId());
            }
            seance.setPatient(patient);
        } else {
            seance.setPatient(null);
        }
        return ResponseEntity.ok(service.enregistrerSeance(seance));
    }

    // Récupérer les séances anonymes
    @GetMapping("/anonymes")
    public List<Seance> getSeancesAnonymes() {
        return service.getSeancesAnonymes();
    }

    // Récupérer toutes les séances
    @GetMapping
    public List<Seance> getAllSeances() {
        return service.getAllSeances();
    }

    // Récupérer toutes les séances d'un patient
    @GetMapping("/patient/{patientId}")
    public List<Seance> getSeancesPatient(@PathVariable Long patientId) {
        return service.getSeancesPatient(patientId);
    }

    // Récupérer une séance par son id
    @GetMapping("/{id}")
    public Seance getSeanceById(@PathVariable Long id) {
        return service.getSeanceById(id);
    }

    // Supprimer une séance
    @DeleteMapping("/{id}")
    public void supprimerSeance(@PathVariable Long id) {
        service.supprimerSeance(id);
    }
}