package finalBW.buildweek.controller;

import finalBW.buildweek.config.EmailSender;
import finalBW.buildweek.entity.Cliente;
import finalBW.buildweek.entity.Ruolo;
import finalBW.buildweek.entity.Utente;
import finalBW.buildweek.exceptions.ForbiddenException;
import finalBW.buildweek.payload.EmailPersonalizzataDTO;
import finalBW.buildweek.payload.TemporaryPasswordDTO;
import finalBW.buildweek.service.ClientiService;
import finalBW.buildweek.service.RuoloService;
import finalBW.buildweek.service.UtenteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/admin_api")
public class AdminController {
    private final UtenteService utenteService;
    private final RuoloService ruoloService;
    private final EmailSender emailSender;
    private final ClientiService cService;

    public AdminController(UtenteService utenteService, RuoloService ruoloService, EmailSender emailSender, ClientiService cService) {
        this.utenteService = utenteService;
        this.ruoloService = ruoloService;
        this.emailSender = emailSender;
        this.cService = cService;
    }

    @DeleteMapping("/{utenteId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUtente(@PathVariable Long utenteId, @AuthenticationPrincipal Utente currentUser) {
        Utente found = utenteService.findById(utenteId);
        if (currentUser.getRuoli().stream().anyMatch(ruolo -> ruolo.getDenominazione().equals("SUPER_ADMIN"))) {
            utenteService.deleteUtente(found);
            return;
        }
        boolean isAdmin = found.getRuoli().stream().anyMatch(ruolo -> ruolo.getDenominazione().equals("ADMIN")) || found.getRuoli().stream().anyMatch(ruolo -> ruolo.getDenominazione().equals("SUPER_ADMIN"));
        if (isAdmin) {
            throw new ForbiddenException("Non hai l'autorità per eliminare un ADMIN");
        }
        utenteService.deleteUtente(found);
    }

    @PatchMapping("/{utenteId}/ruoli/add")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addRuolo(@PathVariable Long utenteId, @RequestParam String ruolo, @AuthenticationPrincipal Utente currentUser) {
        ruolo = ruolo.trim().toUpperCase();
        Utente found = utenteService.findById(utenteId);
        String finalRuolo = ruolo;
        if (found.getRuoli().stream().anyMatch(r -> r.getDenominazione().equals(finalRuolo))) {
            return;
        }


        if (currentUser.getRuoli().stream().anyMatch(r -> r.getDenominazione().equals("SUPER_ADMIN"))) {
            List<Ruolo> nuoviRuoli = found.getRuoli();
            Ruolo ruoloDaAggiungere = ruoloService.findByDenominazione(ruolo);
            if (ruoloDaAggiungere == null) {
                ruoloDaAggiungere = ruoloService.save(new Ruolo(ruolo));
            }
            nuoviRuoli.add(ruoloDaAggiungere);
            found.setRuoli(nuoviRuoli);
            utenteService.update(found);
            return;
        }

        if (currentUser.getRuoli().stream().anyMatch(r -> r.getDenominazione().equals("ADMIN"))) {
            if (Objects.equals(ruolo, "ADMIN") || Objects.equals(ruolo, "SUPER_ADMIN"))
                throw new ForbiddenException("Non hai le autorizzazioni per creare nuovi admin");
            Ruolo ruoloDaAggiungere = ruoloService.findByDenominazione(ruolo);
            if (ruoloDaAggiungere == null) {
                ruoloDaAggiungere = ruoloService.save(new Ruolo(ruolo));
            }
            List<Ruolo> nuoviRuoli = found.getRuoli();
            nuoviRuoli.add(ruoloDaAggiungere);
            found.setRuoli(nuoviRuoli);
            utenteService.update(found);
        }


    }

    @PatchMapping("/{utenteId}/ruoli/remove")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeRuolo(@PathVariable Long utenteId, @RequestParam String ruolo, @AuthenticationPrincipal Utente currentUser) {
        ruolo = ruolo.trim().toUpperCase();
        Utente found = utenteService.findById(utenteId);
        String finalRuolo = ruolo;
        if (found.getRuoli().stream().noneMatch(r -> r.getDenominazione().equals(finalRuolo))) {
            return;
        }
        if (currentUser.getRuoli().stream().anyMatch(r -> r.getDenominazione().equals("SUPER_ADMIN"))) {
            List<Ruolo> ruoliAttuali = found.getRuoli();
            String finalRuolo1 = ruolo;
            ruoliAttuali.removeIf(r -> r.getDenominazione().equals(finalRuolo1));
            found.setRuoli(ruoliAttuali);
            utenteService.update(found);
            return;
        }

        if (currentUser.getRuoli().stream().anyMatch(r -> r.getDenominazione().equals("ADMIN"))) {
            if (Objects.equals(ruolo, "ADMIN") || Objects.equals(ruolo, "SUPER_ADMIN"))
                throw new ForbiddenException("Non hai le autorizzazioni per eliminare il ruolo di admin");
            List<Ruolo> ruoliAttuali = found.getRuoli();
            String finalRuolo2 = ruolo;
            ruoliAttuali.removeIf(r -> r.getDenominazione().equals(finalRuolo2));
            found.setRuoli(ruoliAttuali);
            utenteService.update(found);
        }


    }

    @PatchMapping("/{utenteId}/ruoli/resetpassword")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN')") // 1. Aggiunto ADMIN qui
    @ResponseStatus(HttpStatus.OK)
    public TemporaryPasswordDTO resetPassword(@PathVariable Long utenteId, @AuthenticationPrincipal Utente currentUser) {

        Utente found = utenteService.findById(utenteId);

        // 2. Aggiornata la condizione per includere ADMIN
        boolean isAuthorized = currentUser.getRuoli().stream()
                .anyMatch(r -> r.getDenominazione().equals("SUPER_ADMIN") ||
                        r.getDenominazione().equals("ADMIN"));

        if (isAuthorized) {
            SecureRandom random = new SecureRandom();
            StringBuilder password = new StringBuilder();
            String caratteri = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_+=<>?";

            for (int i = 0; i < 12; i++) {
                int index = random.nextInt(caratteri.length());
                password.append(caratteri.charAt(index));
            }

            String tempPassword = password.toString();
            TemporaryPasswordDTO response = new TemporaryPasswordDTO(tempPassword);
            utenteService.updatePassword(found, tempPassword);

            return response;
        }

        throw new ForbiddenException("Non sei autorizzato a resettare la password");
    }


    @PostMapping("/clienti/{clienteId}/email")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void sendEmailToCliente(
            @PathVariable Long clienteId,
            @RequestBody @Valid EmailPersonalizzataDTO body) {

        Cliente cliente = cService.findById(clienteId);

        emailSender.sendCustomEmail(
                cliente.getEmail(),
                body.subject(),
                body.text()
        );
    }

}



