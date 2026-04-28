package finalBW.buildweek.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import finalBW.buildweek.entity.Ruolo;
import finalBW.buildweek.entity.Utente;
import finalBW.buildweek.exceptions.UnauthorizedException;
import finalBW.buildweek.payload.NuovoUtenteDTO;
import finalBW.buildweek.repository.RuoloRepository;
import finalBW.buildweek.repository.UtenteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class UtenteService {

    private final UtenteRepository uRep;
    private final RuoloRepository rRepository;
    private final PasswordEncoder passwordEncoder;

        this.uRep = uRep;
        this.rRepository = rRepository;
        this.passwordEncoder = passwordEncoder;
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
                passwordEncoder.encode(body.password()),
                body.nome(),
                body.cognome()
        );

        Ruolo ruoloUser = rRepository.findByDenominazione("USER")
                .orElseThrow(() -> new RuntimeException("Ruolo USER non trovato"));

        nuovoUtente.getRuoli().add(ruoloUser);

        return uRep.save(nuovoUtente);
    }

    public Page<Utente> findAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return uRep.findAll(pageable);
    }

    public Utente findById(long id) {
        return uRep.findById(id)
                .orElseThrow(() -> new RuntimeException("Utente con id " + id + " non trovato"));
    }

    public Utente findByEmail(String email) {
        return uRep.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Email o password non corretti"));
    }


    public Utente addAdminRole(long utenteId) {
        Utente utente = this.findById(utenteId);

        Ruolo admin = rRepository.findByDenominazione("ADMIN")
                .orElseThrow(() -> new RuntimeException("Ruolo ADMIN non trovato"));

        if (!utente.getRuoli().contains(admin)) {
            utente.getRuoli().add(admin);
        }

        return uRep.save(utente);
    }
}