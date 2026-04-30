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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

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
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/utenti").permitAll()

                .requestMatchers(HttpMethod.GET,
                        "/clienti/**",
                        "/fatture/**",
                        "/indirizzi/**",
                        "/comuni/**",
                        "/province/**",
                        "/stati-fattura/**"

                )
                .hasAnyAuthority("USER", "ADMIN", "SUPER_ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/utenti/me/avatar") // decidiamo se fargli fare solo questo --> se no tolgo il blocco dopo su /utenti a superadmin
                .authenticated()
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
                        "/utenti/**", // --> questo solo per quelli elencanti sotto
                        "/ruoli/**"
                )
                .hasAnyAuthority("ADMIN", "SUPER_ADMIN")
                .anyRequest().authenticated()
        );

        httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    // PER PROBLEMI DI CORS FRONT END
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "http://localhost:5174"
        ));

        configuration.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "PATCH",
                "DELETE",
                "OPTIONS"
        ));

        configuration.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type"
        ));

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}