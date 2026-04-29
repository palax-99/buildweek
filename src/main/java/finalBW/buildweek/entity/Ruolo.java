package finalBW.buildweek.entity;

import finalBW.buildweek.exceptions.ValidationException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "ruoli")
public class Ruolo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ruolo_denominazione", nullable = false, unique = true)
    private String denominazione;

//    @ManyToMany(mappedBy = "ruoli")  // se vogliamo bidirezionale
//    private List<Utente> utenti = new ArrayList<>();

    public Ruolo(String denominazione) {

        if (denominazione == null || denominazione.isBlank()) {
            throw new ValidationException("Campo obbligatoria");
        }
        this.denominazione = denominazione.toUpperCase();     // se qualcuno mette tipo aDmIn ecc
    }

    public void setDenominazione(String denominazione) {
        if (denominazione == null || denominazione.isBlank()) {
            throw new ValidationException("Campo obbligatoria");
        }

        this.denominazione = denominazione.toUpperCase();
    }

//    public void setUtenti(List<Utente> utenti) {
//        this.utenti = utenti;
//    }

    @Override
    public String toString() {
        return "Ruolo{" +
                "id=" + id +
                ", denominazione='" + denominazione + '\'' +
//                ", utenti=" + utenti +
                '}';
    }
}