package finalBW.buildweek.service;

import finalBW.buildweek.entity.Provincia;
import finalBW.buildweek.repository.ProvinciaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProvinciaService {
    private final ProvinciaRepository provinciaRepository;

    public ProvinciaService(ProvinciaRepository provinciaRepository) {
        this.provinciaRepository = provinciaRepository;
    }

    @Transactional(readOnly = true)
    public long count() {
        return provinciaRepository.count();
    }

    @Transactional
    public void deleteAll() {
        provinciaRepository.deleteAllInBatch();
        provinciaRepository.flush();
    }

    @Transactional
    public Provincia save(Provincia provincia) {
        return provinciaRepository.save(provincia);
    }

    @Transactional
    public void saveAll(List<Provincia> province) {
        for (Provincia provincia : province) {
            provinciaRepository.save(provincia);
        }
    }

    @Transactional
    public void replaceAll(List<Provincia> province) {
        provinciaRepository.deleteAllInBatch();
        provinciaRepository.flush();
        for (Provincia provincia : province) {
            provinciaRepository.save(provincia);
        }
    }

}
