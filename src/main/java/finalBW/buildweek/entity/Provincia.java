package finalBW.buildweek.entity;

import finalBW.buildweek.enumeration.Regione;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "province")
public class Provincia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "provincia_id", nullable = false, updatable = false)
    private Long provinciaId;

    @NotBlank(message = "La sigla è obbligatoria")
    @Size(min = 2, max = 5, message = "La sigla deve avere fra 2 e 5 caratteri")
    @Column(name = "sigla", nullable = false, unique = true)
    private String sigla;

    @NotBlank(message = "Il nome della provincia è obbligatorio")
    @Size(min = 2, max = 255, message = "Il nome della provincia deve avere fra 2 e 255 caratteri")
    @Column(name = "provincia_nome", nullable = false)
    private String provinciaNome;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "La regione è richiesta")
    @Column(name = "regione", nullable = false)
    private Regione regione;

    public Provincia(String sigla, String provinciaNome, Regione regione) {
        this.sigla = sigla;
        this.provinciaNome = provinciaNome;
        this.regione = regione;
    }

    protected Provincia() {
    }

    public Long getProvinciaId() {
        return provinciaId;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getProvinciaNome() {
        return provinciaNome;
    }

    public void setProvinciaNome(String provinciaNome) {
        this.provinciaNome = provinciaNome;
    }

    public Regione getRegione() {
        return regione;
    }

    public void setRegione(Regione regione) {
        this.regione = regione;
    }

    @Override
    public String toString() {
        return "Provincia{" +
                "provinciaId=" + provinciaId +
                ", sigla='" + sigla + '\'' +
                ", provinciaNome='" + provinciaNome + '\'' +
                ", regione=" + regione.getName() +
                '}';
    }
}
