package finalBW.buildweek.service;

import finalBW.buildweek.entity.Ruolo;
import finalBW.buildweek.exceptions.NotFoundException;
import finalBW.buildweek.exceptions.ValidationException;
import finalBW.buildweek.payload.NuovoRuoloDTO;
import finalBW.buildweek.repository.RuoloRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class RuoloService {

    private final RuoloRepository rRepository;

    public RuoloService(RuoloRepository rRepository) {
        this.rRepository = rRepository;
    }

    public Ruolo save(NuovoRuoloDTO body) {

        if (body.denominazione() == null || body.denominazione().isBlank()) {
            throw new ValidationException("Denominazione ruolo obbligatoria");
        }

        String denominazione = body.denominazione().trim().toUpperCase();

        if (rRepository.existsByDenominazione(denominazione)) {
            throw new ValidationException("Il ruolo " + denominazione + " esiste già");
        }

        Ruolo nuovoRuolo = new Ruolo(denominazione);

        return rRepository.save(nuovoRuolo);
    }

    public Page<Ruolo> findAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return rRepository.findAll(pageable);
    }

    public Ruolo findById(long id) {
        return rRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ruolo con id " + id + " non trovato"));
    }


    // Bisognerebbe vedere se non e'associato ecc ... allo stesso modo per ora no update
    public void delete(long id) {
        Ruolo found = this.findById(id);
        rRepository.delete(found);
    }


    public Ruolo findByDenominazione(String ruolo) {
        return rRepository.findByDenominazione(ruolo)
                .orElseThrow(() -> new NotFoundException("Ruolo " + ruolo + " non trovato"));
    }

    public Ruolo save(Ruolo ruolo) {
        return rRepository.save(ruolo);
    }
}