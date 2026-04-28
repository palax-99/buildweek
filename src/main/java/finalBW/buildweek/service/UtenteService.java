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
    private final Cloudinary cloudinaryUploader;

    public UtenteService(UtenteRepository uRep, RuoloRepository rRepository, PasswordEncoder passwordEncoder, Cloudinary cloudinaryUploader) {
        this.uRep = uRep;
        this.rRepository = rRepository;
        this.passwordEncoder = passwordEncoder;
        this.cloudinaryUploader = cloudinaryUploader;
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


    public void avatarUpload(MultipartFile file, long utenteId) {
        if (file.isEmpty()) {
            throw new RuntimeException("Il file è vuoto");
        }
        Utente found = this.findById(utenteId);
        try {
            Map result = cloudinaryUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String avatarUrl = (String) result.get("secure_url");
            found.setAvatar(avatarUrl);
            uRep.save(found);
            System.out.println(avatarUrl);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
}