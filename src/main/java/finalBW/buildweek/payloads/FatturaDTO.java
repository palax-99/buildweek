package finalBW.buildweek.payloads;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record FatturaDTO(
        @NotNull(message = "La data della fattura è obbligatoria")
        @PastOrPresent(message = "La data della fattura non può essere nel futuro")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate data,

        @NotNull(message = "L'importo è obbligatorio")
        @Positive(message = "L'importo deve essere maggiore di zero")
        Double importo,

        @NotNull(message = "Il numero della fattura è obbligatorio")
        @Positive(message = "Il numero della fattura deve essere positivo")
        Long numero,

        @NotNull(message = "L'id del cliente è obbligatorio")
        Long clienteId,

        @NotNull(message = "L'id dello stato fattura è obbligatorio")
        Long statoFatturaId
) {
}
