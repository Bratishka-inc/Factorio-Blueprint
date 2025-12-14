package ru.Factorio_Blueprint.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // CSRF отключен пока разработка
                .csrf(csrf -> csrf.disable())

                // ПРАВИЛА ДОСТУПА
                .authorizeHttpRequests(auth -> auth
                        // ПУБЛИЧНЫЕ СТРАНИЦЫ
                        .requestMatchers(
                                "/", "/auth/**",
                                "/css/**", "/js/**", "/images/**",
                                "/error/**" // доступ к кастомным ошибкам
                        ).permitAll()

                        // ПУБЛИЧНЫЕ GET ЗАПРОСЫ НА ПРОСМОТР ЧЕРТЕЖЕЙ/ГАЙДОВ
                        .requestMatchers(HttpMethod.GET,
                                "/blueprints/**",
                                "/guides/**"
                        ).permitAll()

                        // /add, /edit, /new — ТОЛЬКО ДЛЯ ЗАЛОГИНЕННЫХ
                        .requestMatchers(HttpMethod.GET,
                                "/blueprints/add", "/guides/add",
                                "/guides/new",
                                "/blueprints/*/edit", "/guides/*/edit"
                        ).authenticated()

                        .requestMatchers(HttpMethod.POST,
                                "/blueprints/add", "/guides/add",
                                "/guides/new",
                                "/blueprints/*/edit", "/guides/*/edit"
                        ).authenticated()

                        // DELETE — только для залогиненных
                        .requestMatchers(HttpMethod.POST,
                                "/blueprints/*/delete", "/guides/*/delete"
                        ).authenticated()

                        // АДМИН ПАНЕЛЬ
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // Всё остальное — требует авторизации
                        .anyRequest().authenticated()
                )

                // Если Spring Security ловит AccessDeniedException
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/error/403")
                )

                // ЛОГИН
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/auth/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )

                // ЛОГАУТ
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                )

                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
