package finalBW.buildweek.service;

import finalBW.buildweek.entity.Ruolo;
import finalBW.buildweek.repository.RuoloRepository;
import org.springframework.stereotype.Service;

@Service
public class RuoloService {
    private final RuoloRepository ruoloRepository;

    public RuoloService(RuoloRepository ruoloRepository) {
        this.ruoloRepository = ruoloRepository;
    }


    public Ruolo findByDenominazione(String ruolo) {
        return ruoloRepository.findByDenominazione(ruolo).orElse(null);
    }


    public Ruolo save(Ruolo ruolo) {
        return ruoloRepository.save(ruolo);
    }


}
