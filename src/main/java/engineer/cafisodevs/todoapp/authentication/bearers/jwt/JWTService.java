package engineer.cafisodevs.todoapp.authentication.bearers.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import engineer.cafisodevs.todoapp.account.EntityUser;
import engineer.cafisodevs.todoapp.authentication.bearers.JwtToken;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface JWTService {
    /**
     * This method creates a new JWT token
     * @param email
     * @return String
     */
    String createToken(String email, TokenType tokenType);

    /**
     * This method checks if a token is valid
     *
     * @param token
     * @param userDetails
     * @return boolean
     */
    boolean isTokenValid(String token, UserDetails userDetails);

    /**
     * This method revokes a token
     * @param token
     * @return void
     */
    void revokeToken(String token, EntityUser entityUser);

    /**
     * This method gets the email from a token
     * @param token
     * @return String
     */
    String extractEmail(String token);

    /**
     * This method verifies a token
     * @param token
     * @return DecodedJWT
     */
    DecodedJWT verify(String token);

    /**
     * This method associates a token with a user
     * @param token
     * @param userDetails
     * @return void
     */
    public void associateTokenWithUser(String token, EntityUser entityUser);

    /**
     * This method revokes all tokens for a user
     * @param userDetails
     * @return void
     */
    public void revokeAllTokensForUser(EntityUser entityUser);

    /**
     * This method gets all valid tokens for a user
     * @param userDetails
     * @return List<JwtToken>
     */
    public List<JwtToken> findAllValidTokesByEntityUserUuid(UUID uuid);

    /**
     * This method gets a token by its value
     * @param token
     * @return Optional<JwtToken>
     */
    public Optional<JwtToken> findByToken(String token);

    /**
     * This method generates a token pair
     * @param email
     * @return  Map<TokenType, String>
     */

    public Map<TokenType, String> generateTokenPair(String email);

    /**
     *  This method generates a token pair
     * @param principal
     * @return  Map<TokenType, String>
     */

    public Map<TokenType, String> generateTokenPair(Principal principal);

}
