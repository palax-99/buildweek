package finalBW.buildweek.repository;

import finalBW.buildweek.entity.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UtenteRepository extends JpaRepository<Utente, Long> {

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    Optional<Utente> findByEmail(String email);
}