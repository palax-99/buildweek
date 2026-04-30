package finalBW.buildweek.controller;

import finalBW.buildweek.entity.Utente;
import finalBW.buildweek.exceptions.BadRequestException;
import finalBW.buildweek.payload.LoginDTO;
import finalBW.buildweek.payload.LoginResponseDTO;
import finalBW.buildweek.payload.NewUtenteResponseDTO;
import finalBW.buildweek.payload.NuovoUtenteDTO;
import finalBW.buildweek.service.AuthService;
import finalBW.buildweek.service.UtenteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UtenteService utenteService;

    public AuthController(AuthService authService, UtenteService utenteService) {
        this.authService = authService;
        this.utenteService = utenteService;
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody @Valid LoginDTO body) {
        return authService.login(body);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public NewUtenteResponseDTO saveUtente(@RequestBody @Validated NuovoUtenteDTO nuovoUtenteDTO, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errors = validationResult.getFieldErrors().stream().map(error -> error.getDefaultMessage()).toList();
            throw new BadRequestException(errors);
        }

        Utente newUtente = utenteService.save(nuovoUtenteDTO);
        return new NewUtenteResponseDTO(newUtente.getId());

    }
}