package finalBW.buildweek.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class GeoDataRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        System.out.println("Applicazione avviata!");
    }
}
