package finalBW.buildweek.service;

import finalBW.buildweek.entity.Comune;
import finalBW.buildweek.repository.ComuneRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ComuneService {
    private final ComuneRepository comuneRepository;


    public ComuneService(ComuneRepository comuneRepository) {
        this.comuneRepository = comuneRepository;
    }

    @Transactional
    public void syncAll(List<Comune> comuni) {
        for (Comune comune : comuni) {
            Comune comuneFromDb = comuneRepository.findByComuneNomeIgnoreCaseAndProvinciaProvinciaNomeIgnoreCase(comune.getComuneNome(), comune.getProvincia().getProvinciaNome()).orElse(null);
            if (comuneFromDb == null) {
                comuneRepository.save(comune);
            }
        }
    }
}
