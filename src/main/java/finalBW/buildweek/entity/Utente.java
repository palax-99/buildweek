package finalBW.buildweek.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor
@Entity
@Table(name = "utenti")
public class Utente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false, unique = true)
    private String email;
    @JsonIgnore
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private String cognome;
    @Column(nullable = false)
    private String avatar;
    @ManyToMany
    @JoinTable(name = "ruoli_utenti", joinColumns = @JoinColumn(name = "utente_id"),
            inverseJoinColumns = @JoinColumn(name = "ruolo_id"))
    private List<Ruolo> ruoli = new ArrayList<>();

    public Utente(String username, String email, String password, String nome, String cognome) {

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username obbligatorio");
        }
        if (username.length() < 3) {
            throw new IllegalArgumentException("Username troppo corto");
        }
        this.username = username;

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email obbligatoria");
        }
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Formato email non valido");
        }
        this.email = email;

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password obbligatoria");
        }
        if (password.length() < 6) {
            throw new IllegalArgumentException("La password deve contenere almeno 6 caratteri");
        }
        this.password = password;

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome obbligatorio");
        }
        this.nome = nome;

        if (cognome == null || cognome.isBlank()) {
            throw new IllegalArgumentException("Cognome obbligatorio");
        }
        this.cognome = cognome;

        this.avatar = "https://ui-avatars.com/api/?name=" + nome + "+" + cognome;
    }


    public void setUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username obbligatorio");
        }
        if (username.length() < 3) {
            throw new IllegalArgumentException("Username troppo corto");
        }
        this.username = username;
    }

    public void setEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email obbligatoria");
        }
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Formato email non valido");
        }
        this.email = email;
    }

    public void setPassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password obbligatoria");
        }
        if (password.length() < 6) {
            throw new IllegalArgumentException("La password deve contenere almeno 6 caratteri");
        }
        this.password = password;
    }


    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome obbligatorio");
        }
        this.nome = nome;
        this.avatar = "https://ui-avatars.com/api/?name=" + this.nome + "+" + this.cognome;
    }

    public void setCognome(String cognome) {
        if (cognome == null || cognome.isBlank()) {
            throw new IllegalArgumentException("Cognome obbligatorio");
        }
        this.cognome = cognome;
        this.avatar = "https://ui-avatars.com/api/?name=" + this.nome + "+" + this.cognome;
    }

    public void setAvatar(String avatar) {
        if (avatar == null || avatar.isBlank()) {
            throw new IllegalArgumentException("Avatar obbligatorio");
        }
        this.avatar = avatar;
    }

    // setter per ruoli?
}
