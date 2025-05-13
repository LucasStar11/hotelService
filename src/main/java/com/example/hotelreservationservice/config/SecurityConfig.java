package com.example.hotelreservationservice.config;

import com.example.hotelreservationservice.db.model.UserModel;
import com.example.hotelreservationservice.db.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-resources/**",
                                "/configuration/**",
                                "/swagger-ui/**",
                                "/webjars/**",
                                "/v3/api-docs/**",
                                "/actuator/**",
                                "/error"
                        ).permitAll()
                        .requestMatchers("/api/rooms/available").hasAnyRole("ADMIN", "HOSTESS")
                        .requestMatchers("/api/rooms").hasAnyRole("ADMIN", "HOSTESS")
                        .requestMatchers("/api/bookings").hasAnyRole("ADMIN", "HOSTESS")
                        .requestMatchers("/api/rooms/occupy").hasAnyRole("ADMIN", "HOSTESS")
                        .requestMatchers("/api/rooms/free").hasRole("ADMIN")
                        .anyRequest().permitAll()
                )
                .httpBasic(Customizer.withDefaults())
                .formLogin(AbstractHttpConfigurer::disable)
                .anonymous(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> {
            UserModel user = userRepository.findByUserName(username).orElseThrow(
                    () -> new UsernameNotFoundException("User " + username + " not found")
            );
            if (user == null) throw new UsernameNotFoundException("User not found");
            return User.withUsername(user.getUserName())
                    .password(user.getPassword())
                    .roles(user.getRole().name()) // .replace("ROLE_", "")
                    .build();
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}