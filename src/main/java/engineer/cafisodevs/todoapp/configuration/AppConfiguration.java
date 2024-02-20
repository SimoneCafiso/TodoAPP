package engineer.cafisodevs.todoapp.configuration;

import engineer.cafisodevs.todoapp.account.repository.AccountRepository;
import engineer.cafisodevs.todoapp.exceptions.AccountNotFoundException;
import engineer.cafisodevs.todoapp.rsa.RSAKeyPairsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor

public class AppConfiguration {

    private final AccountRepository repository;

    private final RSAKeyPairsService rsaKeyPairsService;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> repository.findByEmail(username).orElseThrow(AccountNotFoundException::new);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticatonProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(org.springframework.web.servlet.config.annotation.CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*"); // enable cors for all origins
            }
        };
    }



}
