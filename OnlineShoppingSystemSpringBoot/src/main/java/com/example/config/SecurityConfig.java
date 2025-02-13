package com.example.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
        JwtAuthenticationFilter jwtAuthenticationFilter,
        AuthenticationProvider authenticationProvider) 
    {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	 // Building the SecurityFilterChain
     
     http
     .csrf(csrf -> csrf.disable()) // Disable CSRF for stateless APIs
     .authorizeHttpRequests(auth -> auth
         .requestMatchers("/auth/**").permitAll()
         .requestMatchers("/users/**").hasAuthority("ROLE_USER")
         .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
         .anyRequest().authenticated() // Protect all other endpoints
     )
     .sessionManagement(sess -> sess
         .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // No sessions
     )
     .authenticationProvider(authenticationProvider) // Custom authentication provider
     .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
     .cors().configurationSource(corsConfigurationSource());

 return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization","Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**",configuration);

        
        return source;
    }
}
