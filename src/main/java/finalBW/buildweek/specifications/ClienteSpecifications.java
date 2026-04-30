package finalBW.buildweek.specifications;

import finalBW.buildweek.entity.Cliente;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class ClienteSpecifications {
    // Questa classe contiene tanti metodi statici, ognuno produce un "pezzo" di WHERE
    // Poi nel service combinero questi pezzi con .and() per costruire la query finale

    // Filtra i clienti la cui ragione sociale contiene la stringa passata
    // IgnoreCase: faccio il LOWER su entrambi cosi la ricerca non distingue maiuscole/minuscole
    public static Specification<Cliente> ragioneSocialeContiene(String parteDelNome) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("ragioneSociale")), "%" + parteDelNome.toLowerCase() + "%");
        // root.get("ragioneSociale") -> uso il nome del CAMPO JAVA, non della colonna db
        // cb e il CriteriaBuilder, e come una "fabbrica" di pezzi di SQL
        // like + i due % equivale a fare WHERE LOWER(ragione_sociale) LIKE '%mario%'
    }

    // Filtra i clienti con fatturato annuale >= della soglia minima
    public static Specification<Cliente> fatturatoMaggioreUguale(Double minimo) {
        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("fatturatoAnnuale"), minimo);
    }

    // Filtra i clienti con fatturato annuale <= della soglia massima
    // Tenendo i due metodi separati posso combinarli a piacere nel service
    // se l utente passa solo min, solo max, oppure entrambi (= range)
    public static Specification<Cliente> fatturatoMinoreUguale(Double massimo) {
        return (root, query, cb) ->
                cb.lessThanOrEqualTo(root.get("fatturatoAnnuale"), massimo);
    }

    // Filtra i clienti inseriti in una data esatta
    public static Specification<Cliente> dataInserimentoUguale(LocalDate data) {
        return (root, query, cb) ->
                cb.equal(root.get("dataInserimento"), data);
    }

    // Filtra i clienti contattati per l ultima volta in una data esatta
    public static Specification<Cliente> dataUltimoContattoUguale(LocalDate data) {
        return (root, query, cb) ->
                cb.equal(root.get("dataUltimoContatto"), data);
    }
}
