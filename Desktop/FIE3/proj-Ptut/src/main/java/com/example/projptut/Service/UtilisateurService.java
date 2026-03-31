package com.example.projptut.Service;

import com.example.projptut.entity.Administrateur;
import com.example.projptut.entity.Patient;
import com.example.projptut.entity.ProfessionnelDeSante;
import com.example.projptut.entity.Utilisateur;
import com.example.projptut.repository.UtilisateurRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Utilisateur save(Utilisateur user) {
        if (repository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Un compte avec cette adresse email existe déjà.");
        }

        Utilisateur u;
        String encodedPassword = passwordEncoder.encode(user.getMotDePasse());

        if ("patient".equalsIgnoreCase(user.getStatut())) {
            Patient p = new Patient();
            copyUserFields(user, p, encodedPassword);
            u = repository.save(p);
        } else if ("Professionnel".equalsIgnoreCase(user.getStatut())) {
            ProfessionnelDeSante doc = new ProfessionnelDeSante();
            copyUserFields(user, doc, encodedPassword);
            u = repository.save(doc);
        } else if ("Administrateur".equalsIgnoreCase(user.getStatut())) {
            Administrateur admin = new Administrateur();
            copyUserFields(user, admin, encodedPassword);
            u = repository.save(admin);
        } else {
            throw new RuntimeException("Statut inconnu : " + user.getStatut());
        }

        return u;
    }

    private void copyUserFields(Utilisateur source, Utilisateur target, String encodedPassword) {
        target.setNom(source.getNom());
        target.setEmail(source.getEmail());
        target.setMotDePasse(encodedPassword);
        target.setStatut(source.getStatut());
        target.setDateNaissance(source.getDateNaissance());
        target.setSexe(source.getSexe());
    }

    public Utilisateur Login(String email, String motDePasse) {
        return repository.findByEmail(email)
                .filter(u -> passwordEncoder.matches(motDePasse, u.getMotDePasse()))
                .orElse(null);
    }
}