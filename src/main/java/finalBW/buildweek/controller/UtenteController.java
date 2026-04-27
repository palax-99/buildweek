package finalBW.buildweek.controller;

import finalBW.buildweek.entity.Utente;
import finalBW.buildweek.payload.NuovoUtenteDTO;
import finalBW.buildweek.service.UtenteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/utenti")
public class UtenteController {

    private final UtenteService uService;

    private UtenteController(UtenteService uService) {
        this.uService = uService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Utente save(@RequestBody @Valid NuovoUtenteDTO body) {
        return uService.save(body);
    }

    
}
