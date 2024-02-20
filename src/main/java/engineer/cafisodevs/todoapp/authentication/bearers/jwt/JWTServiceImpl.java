package engineer.cafisodevs.todoapp.authentication.bearers.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import engineer.cafisodevs.todoapp.account.EntityUser;
import engineer.cafisodevs.todoapp.authentication.bearers.JwtToken;
import engineer.cafisodevs.todoapp.rsa.RSAKeyPairsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;

@Service
public class JWTServiceImpl implements JWTService {

    //get from application.properties
    @Value("${jwt.accessToken.validity}")
    private long jwtAccessTokenExpirationInMs;

    @Value("${jwt.refreshToken.validity}")
    private long jwtRefreshTokenExpirationInMs;

    private final RSAKeyPairsService rsaKeyPairsService;
    private final JwtRepository jwtRepository;

    public JWTServiceImpl(RSAKeyPairsService rsaKeyPairsService, JwtRepository jwtRepository) {
        this.rsaKeyPairsService = rsaKeyPairsService;
        this.jwtRepository = jwtRepository;
    }

    @Override
    public String createToken(String email, TokenType tokenType) {
        Date expiry = new Date(System.currentTimeMillis() + (tokenType == TokenType.ACCESS ? jwtAccessTokenExpirationInMs : jwtRefreshTokenExpirationInMs));

        assert expiry != null;

        Algorithm algorithm = null;

        try {
            algorithm = Algorithm.RSA512(rsaKeyPairsService.loadPublicKey(), rsaKeyPairsService.loadPrivateKey());

            return JWT.create()
                    .withSubject(email)
                    .withExpiresAt(expiry)
                    .withIssuedAt(new Date(System.currentTimeMillis()))
                    .withIssuer("Todo App Dev Team - Simone C.")
                    .sign(algorithm);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        return extractEmail(token).equals(userDetails.getUsername());
    }

    @Override
    public void revokeToken(String token, EntityUser entityUser) {
        jwtRepository.save(JwtToken.builder()
                .token(token)
                .userAccount(entityUser)
                .expired(true)
                .revoked(true)
                .build());
    }

    @Override
    public String extractEmail(String token) {
        return verify(token).getSubject();
    }

    @Override
    public DecodedJWT verify(String token) {
        try {
            Algorithm algorithm = Algorithm.RSA512(rsaKeyPairsService.loadPublicKey(), rsaKeyPairsService.loadPrivateKey());

            return JWT.require(algorithm)
                    .build()
                    .verify(token);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void associateTokenWithUser(String token, EntityUser entityUser) {
        jwtRepository.save(JwtToken.builder()
                .token(token)
                .userAccount(entityUser)
                .expired(false)
                .revoked(false)
                .build());
    }


    @Override
    public void revokeAllTokensForUser(EntityUser entityUser) {
        jwtRepository.findAllValidTokesByEntityUserUuid(entityUser.getUuid()).forEach(token -> revokeToken(token.getToken(), entityUser));
    }

    @Override
    public List<JwtToken> findAllValidTokesByEntityUserUuid(UUID uuid) {
        return jwtRepository.findAllValidTokesByEntityUserUuid(uuid);
    }

    @Override
    public Optional<JwtToken> findByToken(String token) {
        return jwtRepository.findByToken(token);
    }

    @Override
    public Map<TokenType, String> generateTokenPair(String email) {
        return Map.of(
                TokenType.ACCESS, createToken(email, TokenType.ACCESS),
                TokenType.REFRESH, createToken(email, TokenType.REFRESH)
        );
    }

    @Override
    public Map<TokenType, String> generateTokenPair(Principal principal) {
        return generateTokenPair(principal.getName());
    }
}
