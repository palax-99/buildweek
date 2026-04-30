package finalBW.buildweek.repository;

import finalBW.buildweek.entity.StatoFattura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatoFatturaRepository extends JpaRepository<StatoFattura, Long> {

    Optional<StatoFattura> findByDenominazione(String denominazione);

    boolean existsByDenominazione(String denominazione);
}