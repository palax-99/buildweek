package finalBW.buildweek.repository;

import finalBW.buildweek.entity.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtenteRepository extends JpaRepository<Utente, Long> {
}