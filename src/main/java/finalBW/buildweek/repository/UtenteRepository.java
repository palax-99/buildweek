package finalBW.buildweek.repository;

import finalBW.buildweek.entity.Utente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UtenteRepository extends JpaRepository<Utente, Long> {

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    Optional<Utente> findByEmail(String email);

    @EntityGraph(attributePaths = "ruoli")
    Optional<Utente> findById(Long id);

    Page<Utente> findByRuoli_DenominazioneNotIn(List<String> ruoliEsclusi, Pageable pageable);
}