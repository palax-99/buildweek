package finalBW.buildweek.service;

import finalBW.buildweek.entity.Ruolo;
import finalBW.buildweek.entity.Utente;
import finalBW.buildweek.payload.NuovoUtenteDTO;
import finalBW.buildweek.repository.RuoloRepository;
import finalBW.buildweek.repository.UtenteRepository;
import org.springframework.stereotype.Service;

@Service
public class UtenteService {

    private final UtenteRepository uRep;
    private final RuoloRepository rRepository;

    public UtenteService(UtenteRepository uRep, RuoloRepository rRepository) {
        this.uRep = uRep;
        this.rRepository = rRepository;
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
        Ruolo ruoloUser = rRepository.findByDenominazione("USER")  // devo vedere se esiste non dare per scontato visto che non e' come al solito un enum ma una tabella dati
                .orElseThrow(() -> new RuntimeException("Ruolo USER non trovato")); // Optional Ruolo

        nuovoUtente.getRuoli().add(ruoloUser); // aggiungo alla lista, non set che riscrivo
        return uRep.save(nuovoUtente);
    }
}
