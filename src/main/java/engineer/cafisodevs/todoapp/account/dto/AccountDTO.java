package engineer.cafisodevs.todoapp.account.dto;

import engineer.cafisodevs.todoapp.account.AccountType;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AccountDTO {

    private UUID uuid;

    private String fullName;
    private String email;
    private String password;
    private AccountType accountType;

    public static AccountDTO fromJSON(AccountJSON accountJSON) {
        return AccountDTO.builder()
                .uuid(UUID.fromString(accountJSON.getUuid()))
                .fullName(accountJSON.getFullName())
                .email(accountJSON.getEmail())
                .password(accountJSON.getPassword())
                .accountType(AccountType.valueOf(accountJSON.getAccountType()))
                .build();
    }


}
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
class AccountJSON {

    private String uuid;

    private String fullName;
    private String email;
    private String password;
    private String accountType;


}
