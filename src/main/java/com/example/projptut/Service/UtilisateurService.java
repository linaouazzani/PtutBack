package com.example.projptut.Service;

import com.example.projptut.entity.Administrateur;
import com.example.projptut.entity.Patient;
import com.example.projptut.entity.ProfessionnelDeSante;
import com.example.projptut.entity.Utilisateur;
import com.example.projptut.repository.UtilisateurRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurService {
    private final UtilisateurRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UtilisateurService(UtilisateurRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Utilisateur> getUsers() {
        return repository.findAll();
    }

    /** Inscription publique : force le rôle à "patient" quelle que soit la valeur reçue. */
    public Utilisateur publicRegister(Utilisateur user) {
        user.setStatut("patient");
        return saveWithRole(user);
    }

    /** Création par l'admin : respecte le rôle choisi (patient / Professionnel / Administrateur). */
    public Utilisateur saveByAdmin(Utilisateur user) {
        return saveWithRole(user);
    }

    @Deprecated
    public Utilisateur save(Utilisateur user) {
        return saveWithRole(user);
    }

    private Utilisateur saveWithRole(Utilisateur user) {
        if (repository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Un compte avec cette adresse email existe déjà.");
        }

        Utilisateur u;
        String encodedPassword = passwordEncoder.encode(user.getMotDePasse());

        if ("patient".equalsIgnoreCase(user.getStatut())) {
            Patient p = new Patient();
            copyUserFields(user, p, encodedPassword);
            p.setStatut("patient");
            u = repository.save(p);
        } else if ("Professionnel".equalsIgnoreCase(user.getStatut())) {
            ProfessionnelDeSante doc = new ProfessionnelDeSante();
            copyUserFields(user, doc, encodedPassword);
            doc.setStatut("Professionnel");
            u = repository.save(doc);
        } else if ("Administrateur".equalsIgnoreCase(user.getStatut())) {
            Administrateur admin = new Administrateur();
            copyUserFields(user, admin, encodedPassword);
            admin.setStatut("Administrateur");
            u = repository.save(admin);
        } else {
            throw new RuntimeException("Statut inconnu : " + user.getStatut());
        }

        return u;
    }

    /** Retourne uniquement les patients assignés au professionnel identifié par son email. */
    public List<Patient> getPatientsForPro(String proEmail) {
        Optional<Utilisateur> proOpt = repository.findByEmail(proEmail);
        if (proOpt.isEmpty() || !(proOpt.get() instanceof ProfessionnelDeSante pro)) {
            throw new IllegalArgumentException("Professionnel introuvable : " + proEmail);
        }
        return repository.findPatientsByPro(pro);
    }

    public Utilisateur assignProToPatient(Long patientId, Long proId) {
        Utilisateur patientUser = repository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient introuvable : " + patientId));
        Utilisateur proUser = repository.findById(proId)
                .orElseThrow(() -> new IllegalArgumentException("Professionnel introuvable : " + proId));

        if (!(patientUser instanceof Patient patient)) {
            throw new IllegalArgumentException("L'utilisateur " + patientId + " n'est pas un patient.");
        }
        if (!(proUser instanceof ProfessionnelDeSante pro)) {
            throw new IllegalArgumentException("L'utilisateur " + proId + " n'est pas un professionnel.");
        }

        patient.setProfessionnelDeSante(pro);
        return repository.save(patient);
    }

    private void copyUserFields(Utilisateur source, Utilisateur target, String encodedPassword) {
        target.setNom(source.getNom());
        target.setEmail(source.getEmail());
        target.setMotDePasse(encodedPassword);
        target.setStatut(source.getStatut());
        target.setDateNaissance(source.getDateNaissance());
        target.setSexe(source.getSexe());
    }

    public Utilisateur updateUser(Long id, Utilisateur data) {
        Utilisateur existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable : " + id));
        if (data.getNom() != null && !data.getNom().isBlank()) {
            existing.setNom(data.getNom());
        }
        if (data.getEmail() != null && !data.getEmail().isBlank()) {
            // Vérifie que le nouvel email n'est pas déjà pris par quelqu'un d'autre
            repository.findByEmail(data.getEmail()).ifPresent(u -> {
                if (!u.getId().equals(id)) {
                    throw new IllegalArgumentException("Cet email est déjà utilisé.");
                }
            });
            existing.setEmail(data.getEmail());
        }
        if (data.getMotDePasse() != null && !data.getMotDePasse().isBlank()) {
            existing.setMotDePasse(passwordEncoder.encode(data.getMotDePasse()));
        }
        if (data.getSexe() != null) {
            existing.setSexe(data.getSexe());
        }
        return repository.save(existing);
    }

    public void deleteUser(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Utilisateur introuvable : " + id);
        }
        repository.deleteById(id);
    }

    public Utilisateur Login(String email, String motDePasse) {
        return repository.findByEmail(email)
                .filter(u -> passwordEncoder.matches(motDePasse, u.getMotDePasse()))
                .orElse(null);
    }
}