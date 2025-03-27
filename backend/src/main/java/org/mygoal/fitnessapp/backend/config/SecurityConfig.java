package org.mygoal.fitnessapp.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Configures Spring Security for the application.
 * <p>
 * Enables web security and method security, disables CSRF and CORS protection.
 * Defines the authentication entry point, authentication provider, adds a JWT authentication filter,
 * and sets the session creation policy to stateless.
 * <p>
 * Authorization rules specify which endpoints are publicly accessible and which require authentication.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Tag(name = "Security", description = "Security configuration")
public class SecurityConfig {

    private final UserAuthenticationEntryPoint userAuthenticationEntryPoint;
    private final UserAuthProvider userAuthProvider;

    /**
     * Configures the security filter chain.
     *
     * @param http the {@link HttpSecurity} object to configure security settings
     * @return the configured {@link SecurityFilterChain}
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    @Operation(summary = "Configure the security filter chain")
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling((exceptions) -> exceptions.authenticationEntryPoint(userAuthenticationEntryPoint))

                .addFilterBefore(new JwtAuthFilter(userAuthProvider), BasicAuthenticationFilter.class)
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(HttpMethod.POST, "/api/login", "/api/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/report/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/athletes/{id}", "/api/coaches/{id}").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/athletes/{id}", "/api/coaches/{id}").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/coaches").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/athletes/{id}/params").hasAnyAuthority("ADMIN", "ATHLETE")
                        .requestMatchers(HttpMethod.PATCH, "/api/coaches/{id}/params").hasAnyAuthority("ADMIN", "COACH")
                        .requestMatchers(HttpMethod.POST, "/api/trainings").hasAnyAuthority("COACH", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/trainings/{id}").hasAnyAuthority("COACH", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/trainings/{id}").hasAnyAuthority("COACH", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/trainings/{trainingId}/join/{athleteId}").hasAnyAuthority("ATHLETE", "ADMIN", "COACH")
                        .requestMatchers(HttpMethod.PATCH, "/api/trainings/{trainingId}/cancel/{athleteId}").hasAnyAuthority("ATHLETE", "ADMIN", "COACH")
                        .requestMatchers(HttpMethod.GET, "/api/coaches").hasAnyAuthority("ATHLETE", "COACH", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/coaches/{id}").hasAnyAuthority("ATHLETE", "COACH", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/athletes").hasAnyAuthority("ATHLETE", "COACH", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/athletes/{id}").hasAnyAuthority("ATHLETE", "COACH", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/trainings/{id}").hasAnyAuthority("ATHLETE", "COACH", "ADMIN")
                        .anyRequest().authenticated());

        return http.build();
    }

    /**
     * Configures CORS policy to allow cross-origin requests.
     *
     * @return the configured CORS policy
     */
    @Bean
    @Operation(summary = "Configure CORS policy")
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "http://frontend:3000",
                "http://backend:8080",
                "http://nginx:80"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
