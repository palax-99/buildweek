package finalBW.buildweek.service;

import finalBW.buildweek.entity.Utente;
import finalBW.buildweek.exceptions.UnauthorizedException;
import finalBW.buildweek.payload.LoginDTO;
import finalBW.buildweek.payload.LoginResponseDTO;
import finalBW.buildweek.security.JWTTools;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UtenteService utenteService;
    private final PasswordEncoder passwordEncoder;
    private final JWTTools jwtTools;

    public AuthService(UtenteService utenteService, PasswordEncoder passwordEncoder, JWTTools jwtTools) {
        this.utenteService = utenteService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTools = jwtTools;
    }

    public LoginResponseDTO login(LoginDTO body) {

        Utente utente = utenteService.findByEmail(body.email());

        if (!passwordEncoder.matches(body.password(), utente.getPassword())) {
            throw new UnauthorizedException("Email o password non corretti");
        }

        String token = jwtTools.generateToken(utente);

        return new LoginResponseDTO(token);
    }
}