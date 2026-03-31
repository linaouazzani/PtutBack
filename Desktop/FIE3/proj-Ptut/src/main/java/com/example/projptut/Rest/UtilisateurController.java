package com.example.projptut.Rest;

import com.example.projptut.Configuratio.JwtUtil;
import com.example.projptut.DTO.LoginRequest;
import com.example.projptut.Service.UtilisateurService;
import com.example.projptut.entity.Utilisateur;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Utilisateur utilisateur) {
        try {
            Utilisateur created = service.save(utilisateur);
            return ResponseEntity.ok(created);
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

