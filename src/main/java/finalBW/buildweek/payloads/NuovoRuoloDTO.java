package finalBW.buildweek.payloads;


import jakarta.validation.constraints.NotBlank;

public record NuovoRuoloDTO(
        @NotBlank(message = "Denominazione ruolo obbligatoria")
        String denominazione
) {
}
