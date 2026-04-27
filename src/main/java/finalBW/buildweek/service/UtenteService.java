package finalBW.buildweek.service;

import finalBW.buildweek.entity.Utente;
import finalBW.buildweek.payload.NuovoUtenteDTO;
import finalBW.buildweek.repository.UtenteRepository;
import org.springframework.stereotype.Service;

@Service
public class UtenteService {

    private final UtenteRepository uRep;

    public UtenteService(UtenteRepository uRep) {
        this.uRep = uRep;
    }

    public Utente save(NuovoUtenteDTO body) {

        if (uRep.existsByEmail(body.email())) {
            throw new RuntimeException("L'email " + body.email() + " è già in uso");
        }

        if (uRep.existsByUsername(body.username())) {
            throw new RuntimeException("Username " + body.username() + " già utilizzato");
        }

        Utente nuovoUtente = new Utente(
                body.username(),
                body.email(),
                body.password(),
                body.nome(),
                body.cognome()
        );
// chiedo se vogliono impostar edi base user
        return uRep.save(nuovoUtente);
    }
}
