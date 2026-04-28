package finalBW.buildweek.payload;


import jakarta.validation.constraints.NotBlank;

public record NuovoRuoloDTO(
        @NotBlank(message = "Denominazione ruolo obbligatoria")
        String denominazione
) {
}
