package finalBW.buildweek.data.utility;

import finalBW.buildweek.entity.Comune;
import finalBW.buildweek.entity.Provincia;
import finalBW.buildweek.enumeration.Regione;
import finalBW.buildweek.exception.CsvReadingAndUpdatingProblemException;
import finalBW.buildweek.service.ComuneService;
import finalBW.buildweek.service.ProvinciaService;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static finalBW.buildweek.service.ProvinciaService.PROVINCE_ALIASES;

@Component
public class ComuniCsvReaderAndUpdater {

    private final ComuneService comuneService;
    private final ProvinciaService provinciaService;


    public ComuniCsvReaderAndUpdater(ComuneService comuneService, ProvinciaService provinciaService) {
        this.comuneService = comuneService;
        this.provinciaService = provinciaService;
    }


    public void readAndUpdate() {

        Provincia sudSardegna = new Provincia("SU", "Sud Sardegna", Regione.SARDEGNA);

        if (provinciaService.findBySigla(sudSardegna.getSigla()) == null) {
            provinciaService.save(sudSardegna);
        }


        String file = "src/main/java/finalBW/buildweek/data/csv/comuni-italiani.csv";
        String row;
        int rowNumber = 1;
        List<ComuniCsvRecord> comuniData = new ArrayList<>();
        try (
                BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();
            while ((row = br.readLine()) != null) {
                String[] valori = row.split(";");
                rowNumber += 1;
                if (valori.length < 4) {
                    throw new CsvReadingAndUpdatingProblemException("Riga CSV per i comuni non valida alla riga " + rowNumber + ": " + row);
                }
                comuniData.add(new ComuniCsvRecord(valori[2].trim(), valori[3].trim()));
            }
        } catch (CsvReadingAndUpdatingProblemException e) {
            throw e;
        } catch (IOException e) {
            throw new CsvReadingAndUpdatingProblemException("Lettura del file csv fallita ", e);
        } catch (RuntimeException e) {
            throw new CsvReadingAndUpdatingProblemException("Errore durante la conversione del CSV alla riga " + rowNumber + " - ", e);
        }

        List<Comune> comuni = new ArrayList<>();
        Map<String, Provincia> provinceByName = provinciaService.findAll().stream().collect(Collectors.toMap(
                provincia -> provincia.getProvinciaNome().toLowerCase().trim(),
                provincia -> provincia
        ));

        try {


            for (int i = 0; i < comuniData.size(); i++) {

                String nomeProvincia = comuniData.get(i).provinciaNome().trim();
                String convertedName = PROVINCE_ALIASES.getOrDefault(nomeProvincia, nomeProvincia);
                Provincia provincia = provinceByName.get(convertedName.toLowerCase().trim());
                comuni.add(new Comune(comuniData.get(i).comuneNome(), provincia));

            }
        } catch (RuntimeException e) {
            throw new CsvReadingAndUpdatingProblemException("Dati inseriti non corrispondenti alle province esistenti", e);
        }

        try {

            comuneService.syncAll(comuni);
        } catch (RuntimeException e) {
            throw new CsvReadingAndUpdatingProblemException("Problema nell'aggiornamento del database", e);
        }


    }


}
