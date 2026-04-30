package finalBW.buildweek.payload;

import finalBW.buildweek.enumeration.TipoCliente;
import jakarta.validation.constraints.*;

public record ClienteDTO(
        @NotBlank(message = "La ragione sociale è obbligatoria")
        @Size(min = 2, max = 100, message = "La ragione sociale deve essere compresa tra 2 e 100 caratteri")
        String ragioneSociale,

        @NotBlank(message = "La partita IVA è obbligatoria")
        @Size(min = 11, max = 11, message = "La partita IVA deve essere di 11 caratteri")
        String partitaIva,

        @NotBlank(message = "L'email è obbligatoria")
        @Email(message = "L'email inserita non è nel formato corretto")
        String email,

        @NotNull(message = "Il fatturato annuale è obbligatorio")
        @PositiveOrZero(message = "Il fatturato annuale non può essere negativo")
        Double fatturatoAnnuale,

        @NotBlank(message = "La PEC è obbligatoria")
        @Email(message = "La PEC inserita non è nel formato corretto")
        String pec,

        @NotBlank(message = "Il telefono è obbligatorio")
        @Pattern(regexp = "^[+0-9\\s\\-]{6,20}$", message = "Il telefono non è in un formato valido")
        String telefono,

        @NotBlank(message = "L'email del contatto è obbligatoria")
        @Email(message = "L'email del contatto non è nel formato corretto")
        String emailContatto,

        @NotBlank(message = "Il nome del contatto è obbligatorio")
        @Size(min = 2, max = 30, message = "Il nome del contatto deve essere compreso tra 2 e 30 caratteri")
        String nomeContatto,

        @NotBlank(message = "Il cognome del contatto è obbligatorio")
        @Size(min = 2, max = 30, message = "Il cognome del contatto deve essere compreso tra 2 e 30 caratteri")
        String cognomeContatto,

        @NotBlank(message = "Il telefono del contatto è obbligatorio")
        @Pattern(regexp = "^[+0-9\\s\\-]{6,20}$", message = "Il telefono del contatto non è in un formato valido")
        String telefonoContatto,

        @NotNull(message = "Il tipo cliente è obbligatorio (PA, SAS, SPA, SRL)")
        TipoCliente tipoCliente
) {
}
