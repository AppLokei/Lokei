package Lokei.aplication.infrastructure.config.security;

import Lokei.aplication.infrastructure.shared.ApiErrorResponse;
import tools.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.time.OffsetDateTime;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            UserDetailsService userDetailsService,
            ObjectMapper objectMapper
    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
        this.objectMapper = objectMapper;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http


                .csrf(AbstractHttpConfigurer::disable)

                .headers(headers -> headers
                        .frameOptions(frame -> frame.disable())
                )

                .cors(Customizer.withDefaults())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(authorize -> authorize


                        .requestMatchers("/auth/**").permitAll()


                        .requestMatchers("/h2-console/**").permitAll()


                        .requestMatchers(HttpMethod.POST, "/**").permitAll()


                        .requestMatchers(HttpMethod.PUT, "/usuario/**").permitAll()


                        .requestMatchers(
                                HttpMethod.GET,
                                "/anuncios",
                                "/anuncios/*",
                                "/anuncios/*/disponibilidade",
                                "/anuncios/principais"
                        ).permitAll()

                        .requestMatchers(HttpMethod.GET, "/categorias").permitAll()

                        .requestMatchers(HttpMethod.GET, "/usuario/*").permitAll()

                        .requestMatchers(HttpMethod.GET, "/arquivos/**").permitAll()

                        .requestMatchers("/error").permitAll()
                        .requestMatchers(HttpMethod.GET, "/alugueis-por-usuario").permitAll()
                        .requestMatchers("/alugueis/**").permitAll()


                        .anyRequest().authenticated()
                )

                .authenticationProvider(daoAuthenticationProvider())

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                .exceptionHandling(exception -> exception

                        .authenticationEntryPoint((request, response, authException) ->
                                writeError(
                                        response,
                                        401,
                                        "UNAUTHORIZED",
                                        "Autenticacao obrigatoria."
                                )
                        )

                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                writeError(
                                        response,
                                        403,
                                        "FORBIDDEN",
                                        "Acesso negado."
                                )
                        )
                )

                .build();
    }

    private DaoAuthenticationProvider daoAuthenticationProvider() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(daoAuthenticationProvider());
    }

    private void writeError(
            jakarta.servlet.http.HttpServletResponse response,
            int status,
            String code,
            String message
    ) throws IOException {

        response.setStatus(status);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        objectMapper.writeValue(
                response.getOutputStream(),
                new ApiErrorResponse(
                        OffsetDateTime.now().toString(),
                        code,
                        message
                )
        );
    }
}