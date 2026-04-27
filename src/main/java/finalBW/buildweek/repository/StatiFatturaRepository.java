package finalBW.buildweek.repository;

import finalBW.buildweek.entities.StatoFattura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatiFatturaRepository extends JpaRepository<StatoFattura, Long> {

    Optional<StatoFattura> findByDenominazione(String denominazione);

    boolean existsByDenominazione(String denominazione);
}