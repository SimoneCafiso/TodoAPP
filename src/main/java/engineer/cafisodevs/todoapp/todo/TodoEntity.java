package engineer.cafisodevs.todoapp.todo;

import engineer.cafisodevs.todoapp.account.EntityUser;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TodoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;


    private String name;
    private String detail;

    private Date createdAt;

    @ManyToOne
    @JoinTable(
            name = "todo_user",
            joinColumns = @JoinColumn(name = "todo_uuid"),
            inverseJoinColumns = @JoinColumn(name = "user_uuid")
    )
    private EntityUser createdBy;
}
