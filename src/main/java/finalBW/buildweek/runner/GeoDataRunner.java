package finalBW.buildweek.runner;

import finalBW.buildweek.data.utility.ComuniCsvReaderAndUpdater;
import finalBW.buildweek.data.utility.ProvinceCsvReaderAndUpdater;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class GeoDataRunner implements CommandLineRunner {
    private final ProvinceCsvReaderAndUpdater provinceCsvReaderAndUpdater;
    private final ComuniCsvReaderAndUpdater comuniCsvReaderAndUpdater;

    public GeoDataRunner(ProvinceCsvReaderAndUpdater provinceCsvReaderAndUpdater, ComuniCsvReaderAndUpdater comuniCsvReaderAndUpdater) {
        this.provinceCsvReaderAndUpdater = provinceCsvReaderAndUpdater;
        this.comuniCsvReaderAndUpdater = comuniCsvReaderAndUpdater;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Verifica e aggiornamento dati sulla base dai csv forniti...");
        provinceCsvReaderAndUpdater.readAndUpdate();
        comuniCsvReaderAndUpdater.readAndUpdate();
        System.out.println("Dati aggiornati");

    }
}
