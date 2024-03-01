package engineer.cafisodevs.todoapp.todo.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import engineer.cafisodevs.todoapp.account.services.AccountService;
import engineer.cafisodevs.todoapp.date.DateUtilities;
import engineer.cafisodevs.todoapp.exceptions.AccountNotFoundException;
import engineer.cafisodevs.todoapp.todo.TodoEntity;
import lombok.*;

import java.text.ParseException;
import java.util.UUID;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
public class UniqueTodoDTO {

    private UUID uuid;
    private String name;
    private String detail;
    private String createdAt;
    private UUID createdBy;

    @JsonCreator
    public UniqueTodoDTO(@JsonProperty("name") String name, @JsonProperty("detail") String detail, @JsonProperty("uuid") UUID uuid) {
        this.name = name;
        this.detail = detail;
        this.createdAt = new DateUtilities().todayAsString();
    }


    public TodoEntity toEntity(AccountService accountService) throws ParseException {

        return TodoEntity.builder()
                .uuid(this.uuid)
                .name(this.name)
                .detail(this.detail)
                .createdAt(new DateUtilities().parseFromString(this.createdAt))
                .createdBy(accountService.loadAccount(this.createdBy).orElseThrow((AccountNotFoundException::new)))
                .build();
    }

}
