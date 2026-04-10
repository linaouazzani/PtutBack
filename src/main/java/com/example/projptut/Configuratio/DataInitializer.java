package com.example.projptut.Configuratio;

import com.example.projptut.entity.Administrateur;
import com.example.projptut.repository.UtilisateurRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Crée un compte Administrateur par défaut au démarrage si aucun n'existe.
 *
 * Identifiants par défaut :
 *   Email    : admin@playnride.fr
 *   Mot de passe : Admin1234
 *
 * Changez le mot de passe dès la première connexion.
 */
@Component
public class DataInitializer {

    private final UtilisateurRepository repository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UtilisateurRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void createDefaultAdmin() {
        boolean adminExists = repository.findAll().stream()
                .anyMatch(u -> "Administrateur".equalsIgnoreCase(u.getStatut()));

        if (!adminExists) {
            Administrateur admin = new Administrateur();
            admin.setNom("Admin Principal");
            admin.setEmail("admin@playnride.fr");
            admin.setMotDePasse(passwordEncoder.encode("Admin1234"));
            admin.setStatut("Administrateur");
            repository.save(admin);
            System.out.println("==========================================");
            System.out.println("✅ Admin par défaut créé :");
            System.out.println("   Email    : admin@playnride.fr");
            System.out.println("   Password : Admin1234");
            System.out.println("   ⚠️  Changez ce mot de passe après connexion !");
            System.out.println("==========================================");
        }
    }
}
