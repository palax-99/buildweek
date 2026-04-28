package finalBW.buildweek.specifications;

import finalBW.buildweek.entity.Cliente;
import finalBW.buildweek.entity.Fattura;
import finalBW.buildweek.entity.StatoFattura;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class FatturaSpecifications {
    // Stessa logica di ClienteSpecifications ma applicata alle fatture
    // I filtri richiesti dalle slide: cliente, stato, data, anno, range importi

    // Filtra le fatture appartenenti ad un cliente specifico
    // Passo l oggetto Cliente intero, JPA prendera la sua PK in automatico
    public static Specification<Fattura> appartieneACliente(Cliente cliente) {
        return (root, query, cb) ->
                cb.equal(root.get("cliente"), cliente);
    }

    // Filtra le fatture con un certo stato (es. solo "PAGATA", solo "INSOLUTA"...)
    public static Specification<Fattura> haStato(StatoFattura stato) {
        return (root, query, cb) ->
                cb.equal(root.get("statoFattura"), stato);
    }

    // Filtra le fatture emesse in una data esatta
    public static Specification<Fattura> dataUguale(LocalDate data) {
        return (root, query, cb) ->
                cb.equal(root.get("data"), data);
    }

    // Filtra le fatture emesse in un certo anno
    // Spring Data non ha un "Year" nativo quindi mi costruisco un range:
    // dal 1 gennaio al 31 dicembre di quell anno
    public static Specification<Fattura> annoUguale(int anno) {
        LocalDate inizio = LocalDate.of(anno, 1, 1);
        LocalDate fine = LocalDate.of(anno, 12, 31);
        return (root, query, cb) ->
                cb.between(root.get("data"), inizio, fine);
        // between fa: WHERE data BETWEEN ? AND ?
    }

    // Filtra le fatture con importo >= della soglia minima
    public static Specification<Fattura> importoMaggioreUguale(Double minimo) {
        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("importo"), minimo);
    }

    // Filtra le fatture con importo <= della soglia massima
    // Combinando questi due metodi ottengo il range richiesto dalle slide
    public static Specification<Fattura> importoMinoreUguale(Double massimo) {
        return (root, query, cb) ->
                cb.lessThanOrEqualTo(root.get("importo"), massimo);
    }
}
