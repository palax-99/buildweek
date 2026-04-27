package finalBW.buildweek.data.utility;

import finalBW.buildweek.entity.Provincia;
import finalBW.buildweek.enumeration.Regione;
import finalBW.buildweek.exception.CsvReadingProblemException;
import finalBW.buildweek.service.ProvinciaService;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProvinceCsvReaderAndUpdater {

    private final ProvinciaService provinciaService;

    public ProvinceCsvReaderAndUpdater(ProvinciaService provinciaService) {
        this.provinciaService = provinciaService;
    }

    // il metodo legge il csv, se non ci sono errori verifica che il numero di
    // dati contenuto nel csv corrisponda a quello contenuto nel database,
    // altrimenti azzera il database e salva i nuovi dati


    public void read() {
        String file = "src/main/java/finalBW/buildweek/data/csv/province-italiane.csv";
        String row;
        int rowNumber = 1;
        List<ProvinceCsvRecord> provinceData = new ArrayList<>();
        try (
                BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();
            while ((row = br.readLine()) != null) {
                String[] valori = row.split(";");
                rowNumber += 1;
                if (valori.length < 3) {
                    throw new CsvReadingProblemException("Riga CSV non valida alla riga " + rowNumber + ": " + row);
                }
                provinceData.add(new ProvinceCsvRecord(valori[0].trim(), valori[1].trim(), Regione.fromName(valori[2].trim())));
            }
        } catch (CsvReadingProblemException e) {
            throw e;
        } catch (IOException e) {
            throw new CsvReadingProblemException("Lettura del file csv fallita ", e);
        } catch (RuntimeException e) {
            throw new CsvReadingProblemException("Errore durante la conversione del CSV alla riga " + rowNumber + " - ", e);
        }


        long valoriNelDb = provinciaService.count();
        if (valoriNelDb != provinceData.size()) {
            List<Provincia> province = new ArrayList<>();
            for (int i = 0; i < provinceData.size(); i++) {

                province.add(new Provincia(provinceData.get(i).sigla(), provinceData.get(i).provinciaNome(), provinceData.get(i).regione()));

            }

            provinciaService.replaceAll(province);

        }


    }


}
