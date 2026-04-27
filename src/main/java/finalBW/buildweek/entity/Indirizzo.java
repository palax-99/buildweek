package finalBW.buildweek.entity;

import finalBW.buildweek.enumeration.TipoSede;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "indirizzi", uniqueConstraints = @UniqueConstraint(columnNames = {"cliente_id", "tipo_sede"}))
public class Indirizzo {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "indirizzo_id", nullable = false, updatable = false)
    private Long indirizzoId;
    @NotNull(message = "Il cliente è obbligatorio")
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    @NotBlank(message = "L'indirizzo è obbligatorio")
    @Size(max = 255, message = "L'indirizzo deve avere massimo 255 caratteri")
    @Column(name = "indirizzo", nullable = false)
    private String indirizzo;
    @NotBlank(message = "Il civico è obbligatorio")
    @Size(max = 255, message = "Il civico deve avere massimo 255 caratteri")
    @Column(name = "civico", nullable = false)
    private String civico;
    @Size(max = 255, message = "La località deve avere massimo 255 caratteri")
    @Column(name = "localita")
    private String localita;
    @NotBlank(message = "Il CAP è richiesto")
    @Pattern(regexp = "\\d{5}", message = "Il CAP deve contenere esattamente 5 cifre")
    @Size(min = 5, max = 5, message = "Il CAP deve avere 5 caratteri")
    @Column(name = "cap", nullable = false, length = 5)
    private String cap;
    @NotNull(message = "Il comune è obbligatorio")
    @ManyToOne
    @JoinColumn(name = "comune_id", nullable = false)
    private Comune comune;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Devi indicare il tipo di sede")
    @Column(name = "tipo_sede", nullable = false)
    private TipoSede tipoSede;

    public Indirizzo(Cliente cliente, String indirizzo, String civico, String localita, String cap, Comune comune, TipoSede tipoSede) {
        this.cliente = cliente;
        this.indirizzo = indirizzo;
        this.civico = civico;
        this.localita = localita;
        this.cap = cap;
        this.comune = comune;
        this.tipoSede = tipoSede;
    }

    protected Indirizzo() {
    }

    public Long getIndirizzoId() {
        return indirizzoId;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getCivico() {
        return civico;
    }

    public void setCivico(String civico) {
        this.civico = civico;
    }

    public String getLocalita() {
        return localita;
    }

    public void setLocalita(String localita) {
        this.localita = localita;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public Comune getComune() {
        return comune;
    }

    public void setComune(Comune comune) {
        this.comune = comune;
    }

    public TipoSede getTipoSede() {
        return tipoSede;
    }

    public void setTipoSede(TipoSede tipoSede) {
        this.tipoSede = tipoSede;
    }
}
