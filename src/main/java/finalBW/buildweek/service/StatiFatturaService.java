package finalBW.buildweek.service;

import finalBW.buildweek.entity.StatoFattura;
import finalBW.buildweek.exceptions.BadRequestException;
import finalBW.buildweek.exceptions.NotFoundException;
import finalBW.buildweek.payload.StatoFatturaDTO;
import finalBW.buildweek.repository.StatiFatturaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class StatiFatturaService {

    private final StatiFatturaRepository statiFatturaRepository;

    public StatiFatturaService(StatiFatturaRepository statiFatturaRepository) {
        this.statiFatturaRepository = statiFatturaRepository;
    }

    // CREA un nuovo stato fattura
    public StatoFattura save(StatoFatturaDTO body) {
        // 1. Controllo che non esista gia uno stato con la stessa denominazione
        // (l ho messo unique nell entita, quindi se non controllo qua mi tira un errore brutto dal DB)
        if (this.statiFatturaRepository.existsByDenominazione(body.denominazione()))
            throw new BadRequestException("Lo stato fattura '" + body.denominazione() + "' esiste gia!");

        // 2. Creo l oggetto entita partendo dal DTO
        StatoFattura nuovoStato = new StatoFattura(body.denominazione());

        // 3. Salvo nel db (save fa INSERT se non c e l id, UPDATE se c e)
        StatoFattura statoSalvato = this.statiFatturaRepository.save(nuovoStato);

        // 4. Log per avere traccia di cosa succede
        log.info("Lo stato fattura con id " + statoSalvato.getStatoFatturaId() + " e stato salvato!");

        // 5. Ritorno l oggetto salvato (ora ha anche l id valorizzato dal DB)
        return statoSalvato;
    }

    // LISTA tutti gli stati con paginazione e ordinamento
    public Page<StatoFattura> findAll(int page, int size, String sortBy) {
        // Limito la size per evitare richieste assurde tipo size=10000
        if (size > 100 || size < 0) size = 10;
        if (page < 0) page = 0;

        // Costruisco l oggetto Pageable che dice al repository: pagina X, Y elementi, ordinati per Z
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        // findAll(Pageable) e gia fornito da JpaRepository, non l ho dovuto scrivere
        return this.statiFatturaRepository.findAll(pageable);
    }

    // TROVA singolo stato per id
    public StatoFattura findById(Long statoId) {
        // findById ritorna un Optional, quindi se non lo trova devo gestirlo:
        // orElseThrow lancia l eccezione che gli passo se l Optional e vuoto
        return this.statiFatturaRepository.findById(statoId)
                .orElseThrow(() -> new NotFoundException("Stato fattura con id " + statoId + " non trovato!"));
    }

    // MODIFICA stato esistente
    public StatoFattura findByIdAndUpdate(Long statoId, StatoFatturaDTO body) {
        // 1. Cerco lo stato (se non esiste, findById gia mi lancia NotFoundException)
        StatoFattura trovato = this.findById(statoId);

        // 2. Se sto cambiando la denominazione, controllo che la nuova non sia gia in uso
        // Lo controllo SOLO se sta cambiando, altrimenti se mando lo stesso nome scatta erroneamente
        if (!trovato.getDenominazione().equals(body.denominazione())) {
            if (this.statiFatturaRepository.existsByDenominazione(body.denominazione()))
                throw new BadRequestException("Lo stato fattura '" + body.denominazione() + "' esiste gia!");
        }

        // 3. Aggiorno il campo
        trovato.setDenominazione(body.denominazione());

        // 4. Salvo (qui save fara UPDATE perche l id c e gia)
        StatoFattura aggiornato = this.statiFatturaRepository.save(trovato);

        log.info("Stato fattura con id " + aggiornato.getStatoFatturaId() + " modificato!");
        return aggiornato;
    }

    // CANCELLA stato per id
    public void findByIdAndDelete(Long statoId) {
        // Prima verifico che esista (findById tira NotFoundException se non c e)
        StatoFattura trovato = this.findById(statoId);

        // Poi lo cancello
        this.statiFatturaRepository.delete(trovato);
    }
}
