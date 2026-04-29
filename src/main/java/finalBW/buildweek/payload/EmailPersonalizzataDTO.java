package finalBW.buildweek.payload;

import jakarta.validation.constraints.NotBlank;

public record EmailPersonalizzataDTO(
        @NotBlank(message = "Oggetto obbligatorio")
        String subject,

        @NotBlank(message = "Testo obbligatorio")
        String text
) {
}