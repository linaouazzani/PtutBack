package com.example.projptut.Service;

import com.example.projptut.entity.Seance;
import com.example.projptut.repository.SeanceRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SeanceService {

    private final SeanceRepository repository;

    public SeanceService(SeanceRepository repository) {
        this.repository = repository;
    }

    // Enregistrer une séance
    public Seance enregistrerSeance(Seance seance) {
        return repository.save(seance);
    }

    // Récupérer toutes les séances d'un patient
    public List<Seance> getSeancesPatient(Long patientId) {
        return repository.findByPatientId(patientId);
    }

    // Récupérer une séance par son id
    public Seance getSeanceById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Séance non trouvée"));
    }

    // Supprimer une séance
    public void supprimerSeance(Long id) {
        repository.deleteById(id);
    }
}