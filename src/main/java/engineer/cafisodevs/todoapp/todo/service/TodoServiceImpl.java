package engineer.cafisodevs.todoapp.todo.service;

import engineer.cafisodevs.todoapp.account.EntityUser;
import engineer.cafisodevs.todoapp.account.services.AccountService;
import engineer.cafisodevs.todoapp.account.services.AccountServiceImpl;
import engineer.cafisodevs.todoapp.todo.TodoEntity;
import engineer.cafisodevs.todoapp.todo.dto.TodoDTO;
import engineer.cafisodevs.todoapp.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;
    private final AccountServiceImpl accountService;

    @Override
    public Optional<TodoEntity> createTodoEntity(TodoEntity todoEntity) {
        return Optional.of(todoRepository.save(todoEntity));
    }

    @Override
    public Optional<TodoEntity> createTodoEntity(TodoDTO todoDTO) {
        try {
            return createTodoEntity(todoDTO.toEntity(accountService));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<TodoEntity> updateTodoEntity(TodoEntity todoEntity) {
        return Optional.of(todoRepository.save(todoEntity));
    }

    @Override
    public Optional<TodoEntity> updateTodoEntity(TodoDTO todoDTO) {
        try {
            return updateTodoEntity(todoDTO.toEntity(accountService));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<TodoEntity> loadTodoEntity(UUID uuid) {
        return todoRepository.findById(uuid);
    }

    @Override
    public List<TodoEntity> loadAllTodoEntitiesbyUser(EntityUser entityUser) {
        return loadAllTodoEntitiesbyUserUUID(entityUser.getUuid());
    }

    @Override
    public List<TodoEntity> loadAllTodoEntitiesbyUserPrincipal(Principal principal) {
        if (principal == null) {

            return List.of();
        }
        return loadAllTodoEntitiesbyUserEmail(principal.getName());
    }

    @Override
    public List<TodoEntity> loadAllTodoEntitiesbyUserEmail(String email) {

        List<TodoEntity> tlist;

        try {
            tlist = todoRepository.findAllByEntityUserEmail(email);
        } catch (Exception e) {

            e.printStackTrace();
            tlist = null;
        }


        return tlist;
    }

    @Override
    public List<TodoEntity> loadAllTodoEntitiesbyUserUUID(UUID uuid) {
        return todoRepository.findAllByEntityUserUUID(uuid);
    }

    @Override
    public void deleteTodoEntity(UUID uuid) {
        todoRepository.deleteById(uuid);
    }

    @Override
    public void deleteTodoEntity(TodoEntity todoEntity) {
        todoRepository.delete(todoEntity);
    }

    @Override
    public boolean todoExists(UUID uuid) {
        return todoRepository.existsById(uuid);
    }

    @Override
    public boolean isTodoEntityOwner(UUID uuid, EntityUser entityUser) {
        TodoEntity todo = todoRepository.getReferenceById(uuid);
        return todo != null && todo.getCreatedBy().getUuid().equals(entityUser.getUuid());
    }
}
