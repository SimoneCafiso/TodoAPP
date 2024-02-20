package engineer.cafisodevs.todoapp.security;

import engineer.cafisodevs.todoapp.account.AccountType;
import engineer.cafisodevs.todoapp.authentication.TLogoutHandler;
import engineer.cafisodevs.todoapp.http.filters.OPRJWTFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private final AuthenticationProvider authenticationProvider;
    private final OPRJWTFilter jwtOPRFilter;

    //TODO: logout handler
    private final TLogoutHandler todoLogoutHandler;
    private static final String[] WHITELISTED_URLS = {"/api/v1/auth/**"};


    private static final String[] ADMIN_URLS = {
            "/api/v1/admin/**"
    };

    private static final String[] ACCOUNT_WHITELISTED_URLS = {
            "/api/v1/todo/**",
            "/api/v1/self/**",
            "/test/**"
    };


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(request -> request.requestMatchers(WHITELISTED_URLS).permitAll()
                        .requestMatchers(ADMIN_URLS).hasAnyRole(AccountType.DEVELOPER.name())
                        .requestMatchers(ACCOUNT_WHITELISTED_URLS).hasAnyRole(AccountType.DEVELOPER.name(), AccountType.USER.name())
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtOPRFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout.logoutUrl("/api/v1/auth/logout")
                        .addLogoutHandler(todoLogoutHandler)
                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()));
        //todo: logout

        return http.build();
    }


}
