package finalBW.buildweek.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "clienti")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long clientiId;

    @Column(name = "ragione_sociale", nullable = false)
    private String ragioneSociale;

    @Column(name = "partita_iva", nullable = false, unique = true)
    private String partitaIva;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "data_inserimento", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataInserimento;

    @Column(name = "data_ultimo_contatto")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataUltimoContatto;

    @Column(name = "fatturato_annuale", nullable = false)
    private Double fatturatoAnnuale;

    @Column(nullable = false, unique = true)
    private String pec;

    @Column(nullable = false)
    private String telefono;

    @Column(name = "email_contatto", nullable = false, unique = true)
    private String emailContatto;

    @Column(name = "nome_contatto", nullable = false)
    private String nomeContatto;

    @Column(name = "cognome_contatto", nullable = false)
    private String cognomeContatto;

    @Column(name = "telefono_contatto", nullable = false)
    private String telefonoContatto;

    @Column(name = "logo_aziendale")
    private String logoAziendale;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cliente", nullable = false)
    private TipoCliente tipoCliente;

    public Cliente(String ragioneSociale, String partitaIva, String email,
                   Double fatturatoAnnuale, String pec, String telefono,
                   String emailContatto, String nomeContatto, String cognomeContatto,
                   String telefonoContatto, TipoCliente tipoCliente) {
        this.ragioneSociale = ragioneSociale;
        this.partitaIva = partitaIva;
        this.email = email;
        this.fatturatoAnnuale = fatturatoAnnuale;
        this.pec = pec;
        this.telefono = telefono;
        this.emailContatto = emailContatto;
        this.nomeContatto = nomeContatto;
        this.cognomeContatto = cognomeContatto;
        this.telefonoContatto = telefonoContatto;
        this.tipoCliente = tipoCliente;
    }
}