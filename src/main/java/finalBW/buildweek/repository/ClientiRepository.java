package finalBW.buildweek.repository;

import finalBW.buildweek.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface ClientiRepository extends JpaRepository<Cliente, Long>, JpaSpecificationExecutor<Cliente> {
    // JpaSpecificationExecutor mi serve per usare le Specification, cosi posso fare
    // filtri dinamici combinabili senza scrivere 1000 metodi findBy diversi

    // Questi 4 existsBy mi servono nel service quando salvo un nuovo cliente
    // per controllare che certi campi unique non siano gia presenti nel db
    boolean existsByPartitaIva(String partitaIva);

    boolean existsByEmail(String email);

    boolean existsByPec(String pec);

    boolean existsByEmailContatto(String emailContatto);

    // I metodi finder per i filtri li ho tolti perche adesso uso le Specifications
}
