package engineer.cafisodevs.todoapp.authentication.bearers.jwt;

import engineer.cafisodevs.todoapp.authentication.bearers.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JwtRepository extends JpaRepository<JwtToken, Long> {

    @Query(value =
            "select t from jwt_token t " +
                    "inner join EntityUser u " +
                    "on t.userAccount.uuid = u.uuid " +
                    "where u.uuid = :uuid and (t.expired = false or t.revoked = false)")
    List<JwtToken> findAllValidTokesByEntityUserUuid(UUID uuid);

    Optional<JwtToken> findByToken(String token);


}
