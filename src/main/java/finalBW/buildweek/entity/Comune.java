package finalBW.buildweek.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "comuni", uniqueConstraints = @UniqueConstraint(columnNames = {"comune_nome", "provincia_id"}))
public class Comune {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comune_id", nullable = false, updatable = false)
    private Long comuneId;


    @NotBlank(message = "Il nome del comune è obbligatorio")
    @Size(max = 255, message = "Il nome del comune deve avere massimo 255 caratteri")
    @Column(name = "comune_nome", nullable = false)
    private String comuneNome;

    @NotNull(message = "La provincia è obbligatoria")
    @ManyToOne
    @JoinColumn(name = "provincia_id", nullable = false)
    private Provincia provincia;


    public Comune(String comuneNome, Provincia provincia) {
        this.comuneNome = comuneNome;
        this.provincia = provincia;
    }

    protected Comune() {
    }

    public Long getComuneId() {
        return comuneId;
    }

    public String getComuneNome() {
        return comuneNome;
    }

    public void setComuneNome(String comuneNome) {
        this.comuneNome = comuneNome;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    @Override
    public String toString() {
        return "Comune{" +
                "comuneId=" + comuneId +
                ", comuneNome='" + comuneNome + '\'' +
                ", provincia=" + (provincia != null ? provincia.getProvinciaNome() : null) +
                '}';
    }
}
