package engineer.cafisodevs.todoapp.account.repository;

import engineer.cafisodevs.todoapp.account.EntityUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<EntityUser, UUID> {

    Optional<EntityUser> findByEmail(String email);

    Optional<EntityUser> findByUuid(UUID uuid);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM EntityUser u WHERE u.email = ?1")
    boolean existsByEmail(String email);

}
