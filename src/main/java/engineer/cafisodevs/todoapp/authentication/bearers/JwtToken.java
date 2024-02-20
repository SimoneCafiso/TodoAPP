package engineer.cafisodevs.todoapp.authentication.bearers;

import engineer.cafisodevs.todoapp.account.EntityUser;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "jwt_token")
@Data @Builder @NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class JwtToken {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, columnDefinition = "LONGTEXT")
    @Lob
    private String token;

    public boolean revoked;

    public boolean expired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private EntityUser userAccount;

}
