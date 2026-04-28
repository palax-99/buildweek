package finalBW.buildweek.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {


    @Bean
    public Cloudinary getImageUploader(@Value("${CLOUDINARY_NAME}") String cloudName,
                                       @Value("${CLOUDINARY_API}") String apiKey,
                                       @Value("${CLOUDINARY_API_SECRET}") String apiSecret)
//    System.out.println(cloudName);
//        System.out.println(apiKey);
//        System.out.println(apiSecret);
    {
        Map<String, String> configuration = new HashMap<>();
        configuration.put("cloud_name", cloudName);
        configuration.put("api_key", apiKey);
        configuration.put("api_secret", apiSecret);
        return new Cloudinary(configuration);
    }

}

