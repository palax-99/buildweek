package finalBW.buildweek.security;

import finalBW.buildweek.entity.Utente;
import finalBW.buildweek.service.UtenteService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JWTFilter extends OncePerRequestFilter {

    private final JWTTools jwtTools;
    private final UtenteService utenteService;

    public JWTFilter(JWTTools jwtTools, UtenteService utenteService) {
        this.jwtTools = jwtTools;
        this.utenteService = utenteService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token mancante o formato non corretto.");
            return;
        }

        try {
            String token = authHeader.replace("Bearer ", "");

            jwtTools.verifyToken(token);

            Long utenteId = jwtTools.extractIdFromToken(token);

            Utente utente = utenteService.findById(utenteId);

            List<SimpleGrantedAuthority> authorities = utente.getRuoli()
                    .stream()
                    .map(ruolo -> new SimpleGrantedAuthority(ruolo.getDenominazione()))
                    .toList();

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    utente,
                    null,
                    authorities
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);

        } catch (Exception ex) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token non valido oppure utente non autorizzato.");
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        String method = request.getMethod();

        return method.equals("OPTIONS")   // PER CORS
                || path.startsWith("/auth")
                || path.equals("/error")
                || (path.equals("/utenti") && method.equals("POST"));
    }


}