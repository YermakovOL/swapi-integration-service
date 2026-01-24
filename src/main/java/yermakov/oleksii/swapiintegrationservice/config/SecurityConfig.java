package yermakov.oleksii.swapiintegrationservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Отключаем CSRF, так как для REST API (POST/PUT запросы) он будет мешать
                .csrf(csrf -> csrf.disable())

                // Разрешаем абсолютно все запросы без авторизации
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )

                // Отключаем стандартную форму логина Spring Security
                .formLogin(form -> form.disable())

                // Отключаем Basic Auth (всплывающее окно в браузере)
                .httpBasic(basic -> basic.disable());

        return http.build();
    }
}
