package finalBW.buildweek.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "stati_fattura")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class StatoFattura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long statoFatturaId;

    @Column(nullable = false, unique = true)
    private String denominazione;

    public StatoFattura(String denominazione) {
        this.denominazione = denominazione;
    }
}
