package finalBW.buildweek.service;

import finalBW.buildweek.entity.StatoFattura;
import finalBW.buildweek.repository.StatoFatturaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
public class StatoFatturaService {

    private final StatoFatturaRepository statoFatturaRepository;
    private final Random random = new Random();

    public StatoFatturaService(StatoFatturaRepository statoFatturaRepository) {
        this.statoFatturaRepository = statoFatturaRepository;
    }

    @Transactional
    public void popolaStatiFattura() {

        List<String> stati = List.of(
                "PAGATA",
                "NON_PAGATA",
                "IN_SCADENZA",
                "SCADUTA"
        );

        for (String denominazione : stati) {
            if (!statoFatturaRepository.existsByDenominazione(denominazione)) {
                statoFatturaRepository.save(new StatoFattura(denominazione));
            }
        }
    }

    public StatoFattura getStatoFatturaRandom() {

        long count = statoFatturaRepository.count();

        if (count == 0) {
            throw new RuntimeException("Nessuno stato fattura presente nel database");
        }

        int randomIndex = random.nextInt((int) count);

        return statoFatturaRepository.findAll(PageRequest.of(randomIndex, 1))
                .getContent()
                .get(0);
    }
}