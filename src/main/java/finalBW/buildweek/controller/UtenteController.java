package finalBW.buildweek.controller;

import finalBW.buildweek.entity.Utente;
import finalBW.buildweek.payload.NuovoUtenteDTO;
import finalBW.buildweek.service.UtenteService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/utenti")
public class UtenteController {

    private final UtenteService uService;

    private UtenteController(UtenteService uService) {
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


    @PatchMapping("/{utenteId}/admin")
    public Utente addAdminRole(@PathVariable long utenteId) {
        return uService.addAdminRole(utenteId);
    }
}