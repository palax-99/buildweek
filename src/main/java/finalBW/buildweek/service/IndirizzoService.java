package finalBW.buildweek.service;

import finalBW.buildweek.entity.Indirizzo;
import finalBW.buildweek.repository.IndirizzoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IndirizzoService {

    private final IndirizzoRepository indirizzoRepository;

    public IndirizzoService(IndirizzoRepository indirizzoRepository) {
        this.indirizzoRepository = indirizzoRepository;
    }

    @Transactional
    public Indirizzo salvaIndirizzo(Indirizzo indirizzo) {
        return indirizzoRepository.save(indirizzo);
    }
}