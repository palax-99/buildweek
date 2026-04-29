package finalBW.buildweek.config;

import finalBW.buildweek.entity.Utente;
import finalBW.buildweek.exceptions.InternalServerException;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {
    private final String domainName;
    private final String apiKey;
    private final String from;
    private final String baseUrl;

    public EmailSender(@Value("${mailgun.domain}") String domainName, @Value("${mailgun.api.key}") String apiKey, @Value("${mailgun.from}") String from,
                       @Value("${mailgun.baseurl}") String baseUrl) {

        this.domainName = domainName;
        this.apiKey = apiKey;
        this.from = from;
        this.baseUrl = baseUrl;
    }

    public void sendRegistrationEmail(Utente recipient) {

        String message = "Ciao " + recipient.getNome() +
                "! Ti confermiamo che la registrazione è andata a buon fine, benvenuto! ✨";

        HttpResponse<JsonNode> response = Unirest.post(this.baseUrl + "/v3/" + this.domainName + "/messages")
                .basicAuth("api", this.apiKey)
                .queryString("from", this.from)
                .queryString("to", recipient.getEmail())
                .queryString("subject", "BENVENUTO!")
                .queryString("text", message)
                .asJson();

        if (response.getStatus() >= 400) {
            throw new InternalServerException("Errore invio email: " + response.getBody());
        }
    }
}