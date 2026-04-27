package finalBW.buildweek.service;

import finalBW.buildweek.entity.Provincia;
import finalBW.buildweek.exception.CsvReadingAndUpdatingProblemException;
import finalBW.buildweek.repository.ProvinciaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProvinciaService {
    public static final Map<String, String> PROVINCE_ALIASES = Map.ofEntries(
            Map.entry("Ascoli Piceno", "Ascoli-Piceno"),
            Map.entry("Bolzano/Bozen", "Bolzano"),
            Map.entry("Forlì-Cesena", "Forli-Cesena"),
            Map.entry("La Spezia", "La-Spezia"),
            Map.entry("Monza e della Brianza", "Monza-Brianza"),
            Map.entry("Pesaro e Urbino", "Pesaro-Urbino"),
            Map.entry("Reggio Calabria", "Reggio-Calabria"),
            Map.entry("Reggio nell'Emilia", "Reggio-Emilia"),
            Map.entry("Valle d'Aosta/Vallée d'Aoste", "Aosta"),
            Map.entry("Verbano-Cusio-Ossola", "Verbania"),
            Map.entry("Vibo Valentia", "Vibo-Valentia")
    );
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
        provinciaRepository.saveAll(province);
    }

    @Transactional
    public void replaceAll(List<Provincia> province) {
        provinciaRepository.deleteAllInBatch();
        provinciaRepository.flush();
        provinciaRepository.saveAll(province);
    }

    @Transactional
    public void syncAll(List<Provincia> province) {

        if (provinciaRepository.count() == 0) {
            provinciaRepository.saveAll(province);
            return;
        }

        Map<String, Provincia> provinceFromDb = provinciaRepository.findAll()
                .stream()
                .collect(Collectors.toMap(
                        provincia -> provincia.getSigla().toUpperCase().trim(),
                        provincia -> provincia
                ));

        List<Provincia> provinceDaSalvare = new ArrayList<>();

        for (Provincia provincia : province) {

            String sigla = provincia.getSigla().toUpperCase().trim();

            Provincia provinciaFromDb = provinceFromDb.get(sigla);

            if (provinciaFromDb == null) {
                provinceDaSalvare.add(provincia);
            } else {
                boolean diversa =
                        !provinciaFromDb.getProvinciaNome().equals(provincia.getProvinciaNome())
                                || provinciaFromDb.getRegione() != provincia.getRegione();

                if (diversa) {
                    provinciaFromDb.setProvinciaNome(provincia.getProvinciaNome());
                    provinciaFromDb.setRegione(provincia.getRegione());

                    provinceDaSalvare.add(provinciaFromDb);
                }
            }
        }

        provinciaRepository.saveAll(provinceDaSalvare);
    }

    @Transactional(readOnly = true)
    public Provincia findByProvinciaNomeIgnoreCase(String nome) {
        return provinciaRepository.findByProvinciaNomeIgnoreCase(nome).orElse(null);
    }

    @Transactional(readOnly = true)
    public Provincia findBySigla(String sigla) {
        return provinciaRepository.findBySigla(sigla).orElse(null);
    }

    @Transactional(readOnly = true)
    public Provincia findByProvinciaNameWithAlias(String provinciaName) {
        String convertedName = PROVINCE_ALIASES.getOrDefault(provinciaName.trim(), provinciaName.trim());
        return provinciaRepository.findByProvinciaNomeIgnoreCase(convertedName).orElseThrow(() -> new CsvReadingAndUpdatingProblemException("Errore nel trovare questa provincia: " + provinciaName));
    }

    @Transactional(readOnly = true)
    public List<Provincia> findAll() {
        return provinciaRepository.findAll();
    }
}
