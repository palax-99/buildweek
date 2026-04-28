package finalBW.buildweek.repository;

import finalBW.buildweek.entity.Comune;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ComuneRepository extends JpaRepository<Comune, Long> {

    Optional<Comune> findByComuneNomeIgnoreCaseAndProvinciaProvinciaNomeIgnoreCase(String comuneNome, String provinciaNome);

    @Query("SELECT c FROM Comune c JOIN FETCH c.provincia")
    List<Comune> findAllWithProvincia();

}
