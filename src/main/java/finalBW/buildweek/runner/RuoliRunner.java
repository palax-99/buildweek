package finalBW.buildweek.runner;

import finalBW.buildweek.entity.Ruolo;
import finalBW.buildweek.entity.Utente;
import finalBW.buildweek.repository.RuoloRepository;
import finalBW.buildweek.repository.UtenteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class RuoliRunner implements CommandLineRunner {

    private final RuoloRepository ruoloRepository;
    private final UtenteRepository uRep;
    private final PasswordEncoder passEncoder;

    public RuoliRunner(RuoloRepository ruoloRepository, UtenteRepository uRep, PasswordEncoder passEncoder) {
        this.ruoloRepository = ruoloRepository;
        this.uRep = uRep;
        this.passEncoder = passEncoder;
    }

    @Override
    public void run(String... args) {

        System.out.println("Generazione ruoli: SuperAdmin, Admin, User...");

        Ruolo user = ruoloRepository.findByDenominazione("USER")
                .orElseGet(() -> ruoloRepository.save(new Ruolo("USER")));

        Ruolo admin = ruoloRepository.findByDenominazione("ADMIN")
                .orElseGet(() -> ruoloRepository.save(new Ruolo("ADMIN")));

        Ruolo superAdminRole = ruoloRepository.findByDenominazione("SUPER_ADMIN")
                .orElseGet(() -> ruoloRepository.save(new Ruolo("SUPER_ADMIN")));

        // 🔹 SUPER ADMIN
        if (!uRep.existsByEmail("superadmin@email.com")) {
            Utente superAdmin = new Utente(
                    "superadmin",
                    "superadmin@email.com",
                    passEncoder.encode("superadmin123"),
                    "Super",
                    "Admin"
            );

            superAdmin.getRuoli().add(user);
            superAdmin.getRuoli().add(admin);
            superAdmin.getRuoli().add(superAdminRole);

            uRep.save(superAdmin);
        }

        // 🔹 10 UTENTI BASE
        for (int i = 1; i <= 10; i++) {
            String email = "user" + i + "@email.com";

            if (!uRep.existsByEmail(email)) {
                Utente utenteBase = new Utente(
                        "user" + i,
                        email,
                        passEncoder.encode("password123"),
                        "Nome" + i,
                        "Cognome" + i
                );

                utenteBase.getRuoli().add(user);
                uRep.save(utenteBase);
            }
        }

        // 🔹 5 ADMIN
        for (int i = 1; i <= 5; i++) {
            String email = "admin" + i + "@email.com";

            if (!uRep.existsByEmail(email)) {
                Utente utenteAdmin = new Utente(
                        "admin" + i,
                        email,
                        passEncoder.encode("admin123"),
                        "AdminNome" + i,
                        "AdminCognome" + i
                );

                utenteAdmin.getRuoli().add(user);
                utenteAdmin.getRuoli().add(admin);

                uRep.save(utenteAdmin);
            }
        }

        System.out.println("Ruoli e utenti generati");
    }
}