package finalBW.buildweek.config;

import finalBW.buildweek.entity.Utente;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {
    private final String domainName;
    private final String apiKey;

    public EmailSender(@Value("${mailgun.domain}") String domainName, @Value("${mailgun.api.key}") String apiKey) {
        this.domainName = domainName;
        this.apiKey = apiKey;
    }

    public void sendRegistrationEmail(Utente recipient) {
        HttpResponse<JsonNode> response = Unirest.post("https://api.mailgun.net/v3/" + this.domainName + "/messages")
                .basicAuth("api", this.apiKey)
                .queryString("from", "gi.ravioli@gmail.com") // da metter in env
                .queryString("to", recipient.getEmail())
                .queryString("subject", "BANG BANG!")
                .queryString("text", "Ciao Samuel! Speriamo ti arrivi .. ✨ ")
                .asJson();

        System.out.println(response.getBody()); // DEBUG
    }
}