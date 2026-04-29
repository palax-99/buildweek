package finalBW.buildweek.repository;


import finalBW.buildweek.entity.Cliente;
import finalBW.buildweek.entity.Fattura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FattureRepository extends JpaRepository<Fattura, Long>, JpaSpecificationExecutor<Fattura> {
    // Anche qui aggiungo JpaSpecificationExecutor per i filtri dinamici sulle fatture

    // Questo serve per cancellare in massa tutte le fatture di un cliente
    // quando elimino il cliente (perche la relazione e unidirezionale e quindi
    // il cascade non lo posso mettere sulla collezione, devo gestirlo io a mano)
    void deleteByCliente(Cliente cliente);

    @Query("SELECT COALESCE(MAX(f.numero), 0) FROM Fattura f")
    Long findMaxNumero();
}