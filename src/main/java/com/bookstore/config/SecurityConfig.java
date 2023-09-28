package com.bookstore.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtValidatorFilter jwtValidatorFilter;

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        http.cors(
                cors -> cors.configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.addAllowedOrigin("http://localhost:8085");
                    configuration.setAllowedMethods(List.of("*"));
                    configuration.setAllowedHeaders(List.of("*"));
                    configuration.setMaxAge(3600L);
                    configuration.setExposedHeaders(List.of("Authorization"));
                    configuration.setAllowCredentials(true);
                    return configuration;
                })
        );

        http.authorizeHttpRequests(
                (request)->
                        request
                                .requestMatchers("api/books/download/**")
                                .permitAll()
                                .requestMatchers(HttpMethod.POST, "api/books/**").hasAuthority("ADMIN")
                                .requestMatchers("/accounts/register")
                                .permitAll()
                                .requestMatchers("/accounts/login")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET, "/accounts/**")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET,
                                        "/api/books/**",
                                         "/api/genres/**")
                                .authenticated()
                                .requestMatchers(
                                        "/contacts/**",
                                        "/notices/**"
                                ).permitAll()
                                .requestMatchers(HttpMethod.GET,"/orders/**")
                                .hasRole("USER")
                                .requestMatchers(HttpMethod.GET, "/api/genres").permitAll()
                                .requestMatchers("/orders/**")
                                .hasRole("ADMIN")
                                .anyRequest().hasRole("SUPER_ADMIN")

        );

        http.addFilterBefore(jwtValidatorFilter, UsernamePasswordAuthenticationFilter.class);

        http.csrf(AbstractHttpConfigurer::disable);

        http.formLogin(withDefaults());
        http.httpBasic(withDefaults());



        return http.build();


    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
