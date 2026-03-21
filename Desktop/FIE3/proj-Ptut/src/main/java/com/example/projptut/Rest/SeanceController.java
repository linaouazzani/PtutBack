package com.example.projptut.Rest;

import com.example.projptut.entity.Seance;
import com.example.projptut.Service.SeanceService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/seances")
@CrossOrigin
public class SeanceController {

    private final SeanceService service;

    public SeanceController(SeanceService service) {
        this.service = service;
    }

    // Enregistrer une séance
    @PostMapping
    public Seance enregistrerSeance(@RequestBody Seance seance) {
        return service.enregistrerSeance(seance);
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