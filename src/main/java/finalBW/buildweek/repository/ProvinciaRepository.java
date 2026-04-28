package finalBW.buildweek.repository;

import finalBW.buildweek.entity.Provincia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProvinciaRepository extends JpaRepository<Provincia, Long> {

    Optional<Provincia> findBySigla(String sigla);

    Optional<Provincia> findByProvinciaNomeIgnoreCase(String sigla);
}
