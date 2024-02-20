package engineer.cafisodevs.todoapp.account;

import engineer.cafisodevs.todoapp.account.permission.AccountPermission;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public enum AccountType {
    DEVELOPER(Set.of(
            AccountPermission.OP
    )),

    USER(Set.of(
            AccountPermission.READ,
            AccountPermission.WRITE,
            AccountPermission.DELETE,
            AccountPermission.UPDATE
    ));

    @Getter
    private final Set<AccountPermission> permissions;

    /**
     * This method returns the granted authorities for the user
     * @return List of SimpleGrantedAuthority
     */
    public List<SimpleGrantedAuthority> getGrantedAuthorities(){
        List<SimpleGrantedAuthority> permissions = new java.util.ArrayList<>(getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .toList());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        return permissions;
    }
}
