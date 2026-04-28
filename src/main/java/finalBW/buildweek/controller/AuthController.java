package finalBW.buildweek.controller;

import finalBW.buildweek.payload.LoginDTO;
import finalBW.buildweek.payload.LoginResponseDTO;
import finalBW.buildweek.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody @Valid LoginDTO body) {
        return authService.login(body);
    }
}