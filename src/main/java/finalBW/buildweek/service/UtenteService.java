package finalBW.buildweek.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import finalBW.buildweek.config.EmailSender;
import finalBW.buildweek.entity.Ruolo;
import finalBW.buildweek.entity.Utente;
import finalBW.buildweek.exceptions.InternalServerException;
import finalBW.buildweek.exceptions.NotFoundException;
import finalBW.buildweek.exceptions.UnauthorizedException;
import finalBW.buildweek.exceptions.ValidationException;
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
    private final Cloudinary cloudinaryUploader;
    private final EmailSender emailSender;

    public UtenteService(UtenteRepository uRep, RuoloRepository rRepository, PasswordEncoder passwordEncoder, Cloudinary cloudinaryUploader, EmailSender emailSender) {
        this.uRep = uRep;
        this.rRepository = rRepository;
        this.passwordEncoder = passwordEncoder;
        this.cloudinaryUploader = cloudinaryUploader;
        this.emailSender = emailSender;
    }

    public Utente save(NuovoUtenteDTO body) {

        if (uRep.existsByEmail(body.email())) {
            throw new ValidationException("L'email " + body.email() + " è già in uso");
        }

        if (uRep.existsByUsername(body.username())) {
            throw new ValidationException("Username " + body.username() + " già utilizzato");
        }

        Utente nuovoUtente = new Utente(
                body.username(),
                body.email(),
                passwordEncoder.encode(body.password()),
                body.nome(),
                body.cognome()
        );

        Ruolo ruoloUser = rRepository.findByDenominazione("USER")
                .orElseThrow(() -> new NotFoundException("Ruolo USER non trovato"));

        nuovoUtente.getRuoli().add(ruoloUser);

        Utente utenteSalvato = uRep.save(nuovoUtente);

        this.emailSender.sendRegistrationEmail(utenteSalvato);

        return utenteSalvato;
    }

    public Page<Utente> findAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return uRep.findAll(pageable);
    }

    public Utente findById(long id) {
        return uRep.findById(id)
                .orElseThrow(() -> new NotFoundException("Utente con id " + id + " non trovato"));
    }

    public Utente findByEmail(String email) {
        return uRep.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Email o password non corretti"));
    }


    public Utente avatarUpload(MultipartFile file, long utenteId) {
        if (file.isEmpty()) {
            throw new ValidationException("Il file è vuoto");
        }
        Utente found = this.findById(utenteId);
        try {
            Map result = cloudinaryUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String avatarUrl = (String) result.get("secure_url");
            found.setAvatar(avatarUrl);
            uRep.save(found);
            System.out.println(avatarUrl);

        } catch (IOException e) {
            throw new InternalServerException("Errore durante il caricamento dell avatar");
        }
        return found;
    }


    public Utente addRole(long utenteId, String ruoloNome) {

        Utente utente = this.findById(utenteId);

        Ruolo ruolo = rRepository.findByDenominazione(ruoloNome.toUpperCase())
                .orElseThrow(() -> new NotFoundException("Ruolo non trovato"));

        if (!utente.getRuoli().contains(ruolo)) {
            utente.getRuoli().add(ruolo);
        }

        return uRep.save(utente);
    }

    public void deleteUtente(Long utenteId) {
        Utente found = findById(utenteId);
        uRep.delete(found);
    }

    public void deleteUtente(Utente utente) {
        uRep.delete(utente);
    }

    public Utente update(Utente utente) {
        return uRep.save(utente);
    }

    public void updatePassword(Utente utente, String newPassword) {
        utente.setPassword(passwordEncoder.encode(newPassword));
        uRep.save(utente);
    }
}