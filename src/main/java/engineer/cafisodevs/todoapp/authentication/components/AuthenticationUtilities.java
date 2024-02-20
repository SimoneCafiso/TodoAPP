package engineer.cafisodevs.todoapp.authentication.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import engineer.cafisodevs.todoapp.account.AccountType;
import engineer.cafisodevs.todoapp.account.EntityUser;
import engineer.cafisodevs.todoapp.account.services.AccountServiceImpl;
import engineer.cafisodevs.todoapp.authentication.bearers.jwt.JWTServiceImpl;
import engineer.cafisodevs.todoapp.authentication.bearers.jwt.TokenType;
import engineer.cafisodevs.todoapp.authentication.dto.AuthenticationRequest;
import engineer.cafisodevs.todoapp.authentication.dto.SignUPRequest;
import engineer.cafisodevs.todoapp.exceptions.AccountNotFoundException;
import engineer.cafisodevs.todoapp.exceptions.AuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.antlr.v4.runtime.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LogLevel;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.System.Logger.Level.ERROR;

@Component
public class AuthenticationUtilities {

    private final AccountServiceImpl accountService;

    private final JWTServiceImpl jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationUtilities(AccountServiceImpl accountService, JWTServiceImpl jwtService, AuthenticationManager authenticationManager) {
        this.accountService = accountService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }


    public Map<TokenType, String> authenticate(AuthenticationRequest authenticationRequest) {
        EntityUser account = accountService.loadAccount(authenticationRequest.getEmail()).orElseThrow(AccountNotFoundException::new);

        assert account != null;

        if (!accountService.checkPassword(authenticationRequest.getEmail(), authenticationRequest.getPassword())) {
            throw new AuthenticationException("Error LoggingIn: Invalid Credentials, please double check!");
        }

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        } catch (Exception e) {
            throw new AuthenticationException(e.getMessage());
        }

        jwtService.revokeAllTokensForUser(account);

        Map<TokenType, String> temp = jwtService.generateTokenPair(authenticationRequest.getEmail());

        jwtService.associateTokenWithUser(temp.get(TokenType.ACCESS), account);


        return temp;

    }

    public EntityUser signUP(SignUPRequest signUPRequest) {

        if (accountService.accountExists(signUPRequest.getEmail())) {
            Logger.getAnonymousLogger().log(Level.WARNING, "Email Already exists");
            return null;
        }


        EntityUser entityUser = EntityUser.builder()
                .fullName(signUPRequest.getFullName())
                .email(signUPRequest.getEmail())
                .password(signUPRequest.getPassword())
                .accountType(AccountType.USER)
                .build();

        accountService.createAccount(entityUser);
        return entityUser;

    }


    /*
         TODO:
             CHECK IF THE TOKEN IS AN ACTUAL REFRESH TOKEN AND NOT AN ACCESS ONE
             (ONE WAY POSSIBLE IS TO CHECK WEATHER OR NOT THE TOKEN IS IN THE DATABASE SO THAT IF IT IS WE KNOW FOR SURE IT'S AN ACCESS TOKEN)
     */
    public void refreshToken(HttpServletRequest req, HttpServletResponse response) throws Exception {
        final String authorizationHeader = req.getHeader("Authorization");


        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid Token");
            return;
        }

        final String token = authorizationHeader.substring(7);


        try {
            String email = jwtService.extractEmail(token);

            if (email == null) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid Token");
                return;
            }

            EntityUser account = accountService.loadAccount(email).orElseThrow(AccountNotFoundException::new);

            if (!jwtService.isTokenValid(token, account)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid Token");
                return;
            }

            Map<TokenType, String> tokens = jwtService.generateTokenPair(email);

            jwtService.revokeAllTokensForUser(account);

            jwtService.associateTokenWithUser(tokens.get(TokenType.ACCESS), account);

            Gson gson = new Gson();

            new ObjectMapper().writeValue(response.getOutputStream(), gson.toJson(tokens));
        } catch (Exception e) {
            throw new AuthenticationException(e.getMessage());
        }

    }

}
