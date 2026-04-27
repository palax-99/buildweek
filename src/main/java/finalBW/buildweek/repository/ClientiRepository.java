package finalBW.buildweek.repository;

import finalBW.buildweek.entities.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ClientiRepository extends JpaRepository<Cliente, Long> {

    boolean existsByPartitaIva(String partitaIva);

    boolean existsByEmail(String email);

    boolean existsByPec(String pec);

    boolean existsByEmailContatto(String emailContatto);

    Page<Cliente> findByRagioneSocialeContainingIgnoreCase(String parteDelNome, Pageable pageable);

    Page<Cliente> findByFatturatoAnnualeGreaterThanEqual(Double minimo, Pageable pageable);

    Page<Cliente> findByFatturatoAnnualeBetween(Double min, Double max, Pageable pageable);

    Page<Cliente> findByDataInserimento(LocalDate data, Pageable pageable);

    Page<Cliente> findByDataUltimoContatto(LocalDate data, Pageable pageable);
}
