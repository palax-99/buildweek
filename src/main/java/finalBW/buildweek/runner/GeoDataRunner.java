package finalBW.buildweek.runner;

import finalBW.buildweek.data.utility.ProvinceCsvReaderAndUpdater;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class GeoDataRunner implements CommandLineRunner {
    private final ProvinceCsvReaderAndUpdater provinceCsvReaderAndUpdater;

    public GeoDataRunner(ProvinceCsvReaderAndUpdater provinceCsvReaderAndUpdater) {
        this.provinceCsvReaderAndUpdater = provinceCsvReaderAndUpdater;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Applicazione avviata!");
        provinceCsvReaderAndUpdater.read();

    }
}
