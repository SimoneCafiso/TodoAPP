package engineer.cafisodevs.todoapp.account.permission;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AccountPermission {

    // Admin Permission
    OP("*"),

    //User Permission
    READ("todo:read"),
    WRITE("todo:write"),
    DELETE("todo:delete"),
    UPDATE("todo:update");


    @Getter
    private final String permission;
}
