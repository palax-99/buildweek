package finalBW.buildweek.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JWTFilter jwtFilter;

    public SecurityConfig(JWTFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf(csrf -> csrf.disable());

        httpSecurity.formLogin(formLogin -> formLogin.disable());

        httpSecurity.httpBasic(httpBasic -> httpBasic.disable());

        httpSecurity.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        httpSecurity.cors(Customizer.withDefaults());

        httpSecurity.authorizeHttpRequests(requests -> requests

                .requestMatchers("/auth/**", "/error").permitAll()

                .requestMatchers(HttpMethod.GET,
                        "/clienti/**",
                        "/fatture/**",
                        "/indirizzi/**",
                        "/comuni/**",
                        "/province/**",
                        "/stati-fattura/**"

                )
                .hasAnyAuthority("USER", "ADMIN", "SUPER_ADMIN")

                .requestMatchers(HttpMethod.POST, "/utenti").permitAll() // RIGUARDO POI
                .requestMatchers(HttpMethod.POST, "/clienti/**")
                .hasAnyAuthority("USER", "ADMIN", "SUPER_ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/utenti/*/admin")
                .hasAuthority("SUPER_ADMIN")

                .requestMatchers(
                        "/clienti/**",
                        "/fatture/**",
                        "/indirizzi/**",
                        "/comuni/**",
                        "/province/**",
                        "/stati-fattura/**",
                        "/utenti/**",
                        "/ruoli/**"
                )
                .hasAnyAuthority("ADMIN", "SUPER_ADMIN")
                .anyRequest().authenticated()
        );

        httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

}