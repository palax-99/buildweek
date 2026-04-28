package finalBW.buildweek.service;

import finalBW.buildweek.entity.Comune;
import finalBW.buildweek.repository.ComuneRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ComuneService {
    private final ComuneRepository comuneRepository;


    public ComuneService(ComuneRepository comuneRepository) {
        this.comuneRepository = comuneRepository;
    }

    @Transactional
    public void syncAll(List<Comune> comuni) {

        if (comuneRepository.count() == 0) {
            comuneRepository.saveAll(comuni);
            return;
        }

        Set<String> comuniGiaPresenti = comuneRepository.findAllWithProvincia().stream().map(this::key).collect(Collectors.toSet());
        List<Comune> comuniDaSalvare = new ArrayList<>();
        for (Comune comune : comuni) {
            String key = key(comune);

            if (!comuniGiaPresenti.contains(key)) {
                comuniDaSalvare.add(comune);
                comuniGiaPresenti.add(key);
            }

            comuneRepository.saveAll(comuniDaSalvare);
        }
    }

    private String key(Comune comune) {
        return comune.getComuneNome().toLowerCase().trim() + "|" + comune.getProvincia().getSigla().toLowerCase().trim();
    }

}
