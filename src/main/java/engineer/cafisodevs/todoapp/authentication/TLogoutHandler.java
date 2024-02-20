package engineer.cafisodevs.todoapp.authentication;

import engineer.cafisodevs.todoapp.account.EntityUser;
import engineer.cafisodevs.todoapp.account.services.AccountServiceImpl;
import engineer.cafisodevs.todoapp.authentication.bearers.JwtToken;
import engineer.cafisodevs.todoapp.authentication.bearers.jwt.JWTServiceImpl;
import engineer.cafisodevs.todoapp.authentication.bearers.jwt.JwtRepository;
import engineer.cafisodevs.todoapp.exceptions.AccountNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TLogoutHandler implements LogoutHandler {

    private final JWTServiceImpl jwtService;
    private final AccountServiceImpl accountService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return;
        }

        final String token = authorizationHeader.substring(7);

        JwtToken stored = jwtService.findByToken(token).orElse(null);

        EntityUser account = accountService.loadAccount(request.getUserPrincipal()).orElseThrow(AccountNotFoundException::new);

        if (stored != null) {
            jwtService.revokeToken(token, account);
            SecurityContextHolder.clearContext();
        }
    }
}
