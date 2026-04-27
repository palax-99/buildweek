package finalBW.buildweek.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NuovoUtenteDTO(

        @NotBlank(message = "Username obbligatorio")
        String username,

        @NotBlank(message = "Email obbligatoria")
        @Email(message = "Email non valida")
        String email,

        @NotBlank(message = "Password obbligatoria")
        @Size(min = 6, message = "La password deve avere almeno 6 caratteri")
        String password,

        @NotBlank(message = "Nome obbligatorio")
        String nome,

        @NotBlank(message = "Cognome obbligatorio")
        String cognome
) {
}