package finalBW.buildweek.repository;

import finalBW.buildweek.entity.Comune;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ComuneRepository extends JpaRepository<Comune, Long> {

    Optional<Comune> findByComuneNomeIgnoreCaseAndProvinciaProvinciaNomeIgnoreCase(String comuneNome, String provinciaNome);
}
