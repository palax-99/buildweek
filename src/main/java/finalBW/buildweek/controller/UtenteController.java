package finalBW.buildweek.controller;

import finalBW.buildweek.entity.Utente;
import finalBW.buildweek.payload.AssegnaRuoloDTO;
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
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
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
    @PreAuthorize("#utenteId == authentication.principal.id or hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    // permetto a user di vedere se stesso con principal
    public Utente getById(@PathVariable long utenteId) {
        return this.uService.findById(utenteId);
    }

    @PatchMapping("/me/avatar")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN', 'SUPER_ADMIN')")
    public Utente uploadMyAvatar(@RequestParam("profile_picture") MultipartFile file,
                                 Authentication authentication) {
        Utente utente = (Utente) authentication.getPrincipal();
        return this.uService.avatarUpload(file, utente.getId());
    }

    @PatchMapping("/{utenteId}/ruoli")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public Utente addRole(
            @PathVariable long utenteId,
            @RequestBody @Valid AssegnaRuoloDTO body) {

        return uService.addRole(utenteId, body.ruolo());
    }


}