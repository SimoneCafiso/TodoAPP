package engineer.cafisodevs.todoapp.http.filters;

import engineer.cafisodevs.todoapp.authentication.bearers.jwt.JWTServiceImpl;
import engineer.cafisodevs.todoapp.authentication.bearers.jwt.JwtRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OPRJWTFilter extends OncePerRequestFilter {

    private final JWTServiceImpl jwtService;
    private final UserDetailsService userDetailsService;
    private final JwtRepository jwtRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().contains("/api/v1/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authorizationHeader.substring(7); // 7 is the length of "Bearer "

        logger.debug("Token: " + token);

        final String email;

        try {
            email = jwtService.extractEmail(token);
            logger.debug("Email: " + email);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            boolean isTokenValid = jwtRepository.findByToken(token)
                    .map(jwtToken -> !jwtToken.isExpired() && !jwtToken.isRevoked())
                    .orElse(false);

            if (!isTokenValid) {
                response.sendError(403, "Bad Bearer Token: Token is either expired or revoked.");
                return;
            }


            UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);

            if (jwtService.isTokenValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(userDetails);
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                logger.debug("Authenticated successfully");
            }


        }

        filterChain.doFilter(request, response);
    }
}
