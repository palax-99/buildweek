package finalBW.buildweek.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "fatture")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Fattura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long fatturaId;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate data;

    @Column(nullable = false)
    private Double importo;

    @Column(nullable = false, unique = true)
    private Long numero;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stato_fattura_id", nullable = false)
    private StatoFattura statoFattura;

    public Fattura(LocalDate data, Double importo, Long numero,
                   Cliente cliente, StatoFattura statoFattura) {
        this.data = data;
        this.importo = importo;
        this.numero = numero;
        this.cliente = cliente;
        this.statoFattura = statoFattura;
    }
}
