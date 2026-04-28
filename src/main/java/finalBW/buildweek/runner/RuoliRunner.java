package finalBW.buildweek.runner;

import finalBW.buildweek.entity.Ruolo;
import finalBW.buildweek.entity.Utente;
import finalBW.buildweek.repository.RuoloRepository;
import finalBW.buildweek.repository.UtenteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
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

        Ruolo user = ruoloRepository.findByDenominazione("USER")
                .orElseGet(() -> ruoloRepository.save(new Ruolo("USER")));

        Ruolo admin = ruoloRepository.findByDenominazione("ADMIN")
                .orElseGet(() -> ruoloRepository.save(new Ruolo("ADMIN")));

        Ruolo superAdminRole = ruoloRepository.findByDenominazione("SUPER_ADMIN")
                .orElseGet(() -> ruoloRepository.save(new Ruolo("SUPER_ADMIN")));

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
    }
}