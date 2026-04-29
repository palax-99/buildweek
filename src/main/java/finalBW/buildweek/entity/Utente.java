package finalBW.buildweek.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import finalBW.buildweek.exceptions.ValidationException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Getter
@NoArgsConstructor
@Entity
@Table(name = "utenti")
public class Utente implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false, unique = true)
    @Email
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
            throw new ValidationException("Username obbligatorio");
        }
        if (username.length() < 3) {
            throw new ValidationException("Username troppo corto");
        }
        this.username = username;

        if (email == null || email.isBlank()) {
            throw new ValidationException("Email obbligatoria");
        }
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new ValidationException("Formato email non valido");
        }
        this.email = email;

        if (password == null || password.isBlank()) {
            throw new ValidationException("Password obbligatoria");
        }
        if (password.length() < 6) {
            throw new ValidationException("La password deve contenere almeno 6 caratteri");
        }
        this.password = password;

        if (nome == null || nome.isBlank()) {
            throw new ValidationException("Nome obbligatorio");
        }
        this.nome = nome;

        if (cognome == null || cognome.isBlank()) {
            throw new ValidationException("Cognome obbligatorio");
        }
        this.cognome = cognome;

        this.avatar = "https://ui-avatars.com/api/?name=" + nome + "+" + cognome;
    }


    public void setUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new ValidationException("Username obbligatorio");
        }
        if (username.length() < 3) {
            throw new ValidationException("Username troppo corto");
        }
        this.username = username;
    }

    public void setEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new ValidationException("Email obbligatoria");
        }
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new ValidationException("Formato email non valido");
        }
        this.email = email;
    }

    public void setPassword(String password) {
        if (password == null || password.isBlank()) {
            throw new ValidationException("Password obbligatoria");
        }
        if (password.length() < 6) {
            throw new ValidationException("La password deve contenere almeno 6 caratteri");
        }
        this.password = password;
    }


    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new ValidationException("Nome obbligatorio");
        }
        this.nome = nome;
        this.avatar = "https://ui-avatars.com/api/?name=" + this.nome + "+" + this.cognome;
    }

    public void setCognome(String cognome) {
        if (cognome == null || cognome.isBlank()) {
            throw new ValidationException("Cognome obbligatorio");
        }
        this.cognome = cognome;
        this.avatar = "https://ui-avatars.com/api/?name=" + this.nome + "+" + this.cognome;
    }

    public void setAvatar(String avatar) {
        if (avatar == null || avatar.isBlank()) {
            throw new ValidationException("Avatar obbligatorio");
        }
        this.avatar = avatar;
    }

    public void setRuoli(List<Ruolo> ruoli) {
        if (ruoli == null || ruoli.isEmpty()) { // vogliamo o non vogliamo che possa nascere senza ruoli? cosi tolgo empty
            throw new ValidationException("Assegna un ruolo");
        }

        this.ruoli = ruoli;
    }


    @Override
    public String toString() {
        return "Utente{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +

                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +

                ", ruoli=" + ruoli +
                '}';
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return ruoli.stream().map(ruolo -> new SimpleGrantedAuthority(ruolo.getDenominazione())).toList();
    }
}
