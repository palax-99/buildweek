package finalBW.buildweek.service;

import finalBW.buildweek.entity.Cliente;
import finalBW.buildweek.entity.Fattura;
import finalBW.buildweek.entity.StatoFattura;
import finalBW.buildweek.exceptions.NotFoundException;
import finalBW.buildweek.payload.FatturaDTO;
import finalBW.buildweek.repository.FattureRepository;
import finalBW.buildweek.specifications.FatturaSpecifications;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class FattureService {

    private final FattureRepository fattureRepository;
    private final ClientiService clientiService;
    private final StatiFatturaService statiFatturaService;
    // Mi servono i Service degli altri due (e non i Repository) perche' cosi'
    // riuso la loro logica di findById che gia' tira NotFoundException se non trova niente

    public FattureService(FattureRepository fattureRepository,
                          ClientiService clientiService,
                          StatiFatturaService statiFatturaService) {
        this.fattureRepository = fattureRepository;
        this.clientiService = clientiService;
        this.statiFatturaService = statiFatturaService;
    }

    // CREA una nuova fattura
    public Fattura save(FatturaDTO body) {
        // 1. Recupero il cliente dal db (se non esiste, findById lancia gia' NotFoundException)
        Cliente cliente = this.clientiService.findById(body.clienteId());

        // 2. Recupero lo stato fattura dal db (stessa logica)
        StatoFattura stato = this.statiFatturaService.findById(body.statoFatturaId());

        // 3. Costruisco l entita' Fattura
        // Notare: passo gli OGGETTI cliente e stato (non gli id!)
        // Perche' nell entita' Fattura ho @ManyToOne Cliente, non Long clienteId
        Fattura nuovaFattura = new Fattura(
                body.data(),
                body.importo(),
                body.numero(),
                cliente,
                stato
        );

        // 4. Salvo nel db
        Fattura fatturaSalvata = this.fattureRepository.save(nuovaFattura);

        // 5. Log
        log.info("Fattura con id " + fatturaSalvata.getFatturaId() + " salvata correttamente");

        return fatturaSalvata;
    }

    // LISTA tutte le fatture senza filtri (paginazione + ordinamento)
    public Page<Fattura> findAll(int page, int size, String sortBy) {
        if (size > 100 || size < 0) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.fattureRepository.findAll(pageable);
    }

    // LISTA con filtri dinamici (uso le Specification!)
    // Soddisfa la task "Filtrare le fatture" delle slide
    public Page<Fattura> findFiltered(
            Long clienteId,
            Long statoFatturaId,
            LocalDate data,
            Integer anno,
            Double importoMin,
            Double importoMax,
            int page, int size, String sortBy) {

        if (size > 100 || size < 0) size = 10;
        if (page < 0) page = 0;

        // Parto da Specification "vuota"
        Specification<Fattura> spec = Specification.unrestricted();

        // Per ogni parametro non null aggiungo il pezzo di WHERE corrispondente
        if (clienteId != null) {
            // Mi servo del Service per recuperare il Cliente (anche per validarlo)
            Cliente cliente = this.clientiService.findById(clienteId);
            spec = spec.and(FatturaSpecifications.appartieneACliente(cliente));
        }
        if (statoFatturaId != null) {
            StatoFattura stato = this.statiFatturaService.findById(statoFatturaId);
            spec = spec.and(FatturaSpecifications.haStato(stato));
        }
        if (data != null) {
            spec = spec.and(FatturaSpecifications.dataUguale(data));
        }
        if (anno != null) {
            // Il filtro per anno lo gestisco con un range di date
            // Spring Data non ha un metodo nativo per filtrare per anno
            spec = spec.and(FatturaSpecifications.annoUguale(anno));
        }
        if (importoMin != null) {
            spec = spec.and(FatturaSpecifications.importoMaggioreUguale(importoMin));
        }
        if (importoMax != null) {
            spec = spec.and(FatturaSpecifications.importoMinoreUguale(importoMax));
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.fattureRepository.findAll(spec, pageable);
    }

    // TROVA singola fattura per id
    public Fattura findById(Long fatturaId) {
        return this.fattureRepository.findById(fatturaId)
                .orElseThrow(() -> new NotFoundException("Fattura con id " + fatturaId + " non trovata!"));
    }

    // MODIFICA fattura esistente
    public Fattura findByIdAndUpdate(Long fatturaId, FatturaDTO body) {
        // 1. Cerco la fattura
        Fattura trovata = this.findById(fatturaId);

        // 2. Recupero il cliente e lo stato dai loro service
        // (se gli id non sono validi, ottengo NotFoundException)
        Cliente cliente = this.clientiService.findById(body.clienteId());
        StatoFattura stato = this.statiFatturaService.findById(body.statoFatturaId());

        // 3. Aggiorno tutti i campi modificabili
        trovata.setData(body.data());
        trovata.setImporto(body.importo());
        trovata.setNumero(body.numero());
        trovata.setCliente(cliente);
        trovata.setStatoFattura(stato);

        // 4. Salvo (qui save fara' UPDATE perche' l id c e gia')
        Fattura aggiornata = this.fattureRepository.save(trovata);

        log.info("Fattura con id " + aggiornata.getFatturaId() + " modificata correttamente");
        return aggiornata;
    }

    // MODIFICA solo lo stato della fattura (operazione frequente nel mondo reale)
    // Esempio: cliente paga -> aggiorno stato a "PAGATA"
    public Fattura findByIdAndUpdateStato(Long fatturaId, Long nuovoStatoId) {
        Fattura trovata = this.findById(fatturaId);
        StatoFattura nuovoStato = this.statiFatturaService.findById(nuovoStatoId);

        trovata.setStatoFattura(nuovoStato);

        Fattura aggiornata = this.fattureRepository.save(trovata);
        log.info("Stato della fattura " + fatturaId + " aggiornato a " + nuovoStato.getDenominazione());
        return aggiornata;
    }

    // CANCELLA fattura per id
    public void findByIdAndDelete(Long fatturaId) {
        Fattura trovata = this.findById(fatturaId);
        this.fattureRepository.delete(trovata);
    }

    public Long getUltimoNumeroFattura() {
        return fattureRepository.findMaxNumero();
    }

    @Transactional
    public List<Fattura> salvaFatture(List<Fattura> fatture) {
        return fattureRepository.saveAll(fatture);
    }

}
