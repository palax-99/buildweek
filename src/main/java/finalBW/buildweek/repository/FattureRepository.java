package finalBW.buildweek.repository;


import finalBW.buildweek.entities.Cliente;
import finalBW.buildweek.entities.Fattura;
import finalBW.buildweek.entities.StatoFattura;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface FattureRepository extends JpaRepository<Fattura, Long> {

    Page<Fattura> findByCliente(Cliente cliente, Pageable pageable);

    Page<Fattura> findByStatoFattura(StatoFattura statoFattura, Pageable pageable);

    Page<Fattura> findByData(LocalDate data, Pageable pageable);

    Page<Fattura> findByDataBetween(LocalDate inizio, LocalDate fine, Pageable pageable);

    Page<Fattura> findByImportoBetween(Double min, Double max, Pageable pageable);

    void deleteByCliente(Cliente cliente);
}
