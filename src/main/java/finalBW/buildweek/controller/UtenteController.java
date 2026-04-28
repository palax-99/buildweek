package finalBW.buildweek.controller;

import finalBW.buildweek.entity.Utente;
import finalBW.buildweek.payload.NuovoUtenteDTO;
import finalBW.buildweek.service.UtenteService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/utenti")
public class UtenteController {

    private final UtenteService uService;

    public UtenteController(UtenteService uService) {
        this.uService = uService;
    }


    @GetMapping
    public Page<Utente> findAll(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "5") int size,
                                @RequestParam(defaultValue = "cognome") String sortBy) {
        return uService.findAll(page, size, sortBy);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Utente save(@RequestBody @Valid NuovoUtenteDTO body) {
        return uService.save(body);
    }

    @GetMapping("/{utenteId}")
    public Utente getById(@PathVariable long utenteId) {
        return this.uService.findById(utenteId);  // FACCIO DTO RISPOSTA SE VOGLIO DIVERSO
    }

    @PatchMapping("/me/avatar")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN', 'SUPER_ADMIN')")
    public Utente uploadMyAvatar(@RequestParam("profile_picture") MultipartFile file,
                                 Authentication authentication) {
        Utente utente = (Utente) authentication.getPrincipal();
        return this.uService.avatarUpload(file, utente.getId());
    }

    @PatchMapping("/{utenteId}/admin")
    // @PreAuthorize("hasAnyAuthory('ADMIN', 'SUPER_ADMIN')") ----> Ti
    public Utente addAdminRole(@PathVariable long utenteId) {
        return uService.addAdminRole(utenteId);
    }


    // --> Verifico ruoli .. GET --> clienti totali, get clienti per id , salva cliente. Inietto service di cliente --> DA FARE SU CLINETI CONTROLLER PERO'

    // MAIL --> lun/mar/mer ... mercoledi' mattina repo
}