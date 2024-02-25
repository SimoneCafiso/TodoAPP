package engineer.cafisodevs.todoapp.account;

import engineer.cafisodevs.todoapp.todo.TodoEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "user_account")
@Data @Builder @NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class EntityUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    private String fullName;

    @Column(unique = true)
    private String email;

    private String password;


    @Enumerated(EnumType.STRING)
    private AccountType accountType;


    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TodoEntity> todos;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return accountType.getGrantedAuthorities();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
