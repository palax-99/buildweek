package finalBW.buildweek.data.utility;

import net.datafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
public class FakerConfig {

    @Bean
    public Faker faker() {
        return new Faker();
    }

    @Bean
    public Random random() {
        return new Random();
    }
}