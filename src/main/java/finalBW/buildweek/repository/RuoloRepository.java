package finalBW.buildweek.repository;

import finalBW.buildweek.entity.Ruolo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RuoloRepository extends JpaRepository<Ruolo, Long> {

    Optional<Ruolo> findByDenominazione(String denominazione);
}