package finalBW.buildweek.payload;

import jakarta.validation.constraints.NotBlank;

public record AssegnaRuoloDTO(
        @NotBlank(message = "Il ruolo è obbligatorio")
        String ruolo
) {
}