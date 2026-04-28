package finalBW.buildweek.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record StatoFatturaDTO(
        @NotBlank(message = "La denominazione dello stato è obbligatoria")
        @Size(min = 2, max = 50, message = "La denominazione deve essere compresa tra 2 e 50 caratteri")
        String denominazione
) {
}