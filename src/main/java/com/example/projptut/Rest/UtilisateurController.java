package com.example.projptut.Rest;

import com.example.projptut.Configuratio.JwtUtil;
import com.example.projptut.DTO.LoginRequest;
import com.example.projptut.Service.UtilisateurService;
import com.example.projptut.entity.Patient;
import com.example.projptut.entity.Utilisateur;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/utilisateurs")
@CrossOrigin

public class UtilisateurController {
    private final UtilisateurService service;
    private  final JwtUtil jwtUtil;

    public UtilisateurController(UtilisateurService service, JwtUtil jwtUtil) {
        this.service = service;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public List<Utilisateur> GetUsers(){
        return service.getUsers();
    }
    /** Inscription publique — le rôle est forcé à "patient" côté service. */
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Utilisateur utilisateur) {
        try {
            service.publicRegister(utilisateur);
            Map<String, String> ok = new HashMap<>();
            ok.put("message", "Inscription réussie");
            return ResponseEntity.ok(ok);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Une erreur est survenue lors de la création du compte.");
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /** Création par l'administrateur — le rôle (statut) est respecté tel quel. */
    @PostMapping("/admin")
    public ResponseEntity<?> createByAdmin(@Valid @RequestBody Utilisateur utilisateur) {
        try {
            service.saveByAdmin(utilisateur);
            Map<String, String> ok = new HashMap<>();
            ok.put("message", "Inscription réussie");
            return ResponseEntity.ok(ok);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Une erreur est survenue lors de la création du compte.");
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * Retourne uniquement les patients assignés au professionnel connecté.
     * Utilise l'email extrait du JWT via l'Authentication Spring Security.
     */
    @GetMapping("/mes-patients")
    public ResponseEntity<?> getMesPatients(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).body(Map.of("message", "Non authentifié"));
        }
        try {
            List<Patient> patients = service.getPatientsForPro(authentication.getName());
            return ResponseEntity.ok(patients);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    /** Associer un patient à un professionnel de santé. */
    @PutMapping("/patients/{patientId}/assign-pro/{proId}")
    public ResponseEntity<?> assignPro(@PathVariable Long patientId, @PathVariable Long proId) {
        try {
            Utilisateur updated = service.assignProToPatient(patientId, proId);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    /** Mise à jour du profil (nom, email, motDePasse, sexe). */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Utilisateur utilisateur) {
        try {
            Utilisateur updated = service.updateUser(id, utilisateur);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    /** Suppression d'un utilisateur par l'administrateur. */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            service.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest loginRequest){
        Utilisateur user=service.Login(loginRequest.getEmail(),loginRequest.getMotDePasse());
        if(user==null){
            throw new RuntimeException("Email ou mot de passe incorrect !");
        }
        String token=jwtUtil.generateToken(user.getEmail());
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("email", user.getEmail());
        response.put("nom", user.getNom());
        response.put("statut", user.getStatut());
        response.put("id", String.valueOf(user.getId()));
        return response;

        }
    }

