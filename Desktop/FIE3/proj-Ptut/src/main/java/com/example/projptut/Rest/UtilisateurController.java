package com.example.projptut.Rest;

import com.example.projptut.Configuratio.JwtUtil;
import com.example.projptut.DTO.LoginRequest;
import com.example.projptut.Service.UtilisateurService;
import com.example.projptut.entity.Utilisateur;
import jakarta.validation.Valid;
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
    public Utilisateur create(@Valid @RequestBody Utilisateur utilisateur) {
        return service.save(utilisateur);
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

