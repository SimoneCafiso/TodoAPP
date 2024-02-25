package engineer.cafisodevs.todoapp.todo.service;

import engineer.cafisodevs.todoapp.account.EntityUser;
import engineer.cafisodevs.todoapp.todo.TodoEntity;
import engineer.cafisodevs.todoapp.todo.dto.TodoDTO;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TodoService {


    /**
     * This method creates a new todo
     * @param todoEntity
     * @return Optional of TodoEntity
     */
    public Optional<TodoEntity> createTodoEntity(TodoEntity todoEntity);


    /**
     * This method creates a new todo
     * @param todoDTO
     * @return Optional of TodoEntity
     */
    public Optional<TodoEntity> createTodoEntity(TodoDTO todoDTO);


    /**
     * This method updates a todo
     * @param todoEntity
     * @return optional of TodoEntity
     */
    public Optional<TodoEntity> updateTodoEntity(TodoEntity todoEntity);

    /**
     * This method updates a todo
     * @param todoDTO
     * @return optional of TodoEntity
     */
    public Optional<TodoEntity> updateTodoEntity(TodoDTO todoDTO);

    /**
     * This method loads a todo
     * @param uuid
     * @return Optional of TodoEntity
     */
    public Optional<TodoEntity> loadTodoEntity(UUID uuid);
    /**
     * This method loads all todos for a user
     * @param entityUser
     * @return List of TodoEntity
     */
    public List<TodoEntity> loadAllTodoEntitiesbyUser(EntityUser entityUser);
    /**
     * This method loads all todos for a user
     * @param principal
     * @return List of TodoEntity
     */
    public List<TodoEntity> loadAllTodoEntitiesbyUserPrincipal(Principal principal);
    /**
     * This method loads all todos for a user
     * @param email
     * @return List of TodoEntity
     */
    public List<TodoEntity> loadAllTodoEntitiesbyUserEmail(String email);

    /**
     * This method loads all todos for a user
     * @param uuid
     * @return List of TodoEntity
     */

    public List<TodoEntity> loadAllTodoEntitiesbyUserUUID(UUID uuid);

    /**
     * This method deletes a todo
     * @param uuid
     */

    public void deleteTodoEntity(UUID uuid);

    /**
     * This method deletes a todo
     * @param todoEntity
     */

    public void deleteTodoEntity(TodoEntity todoEntity);

    /**
     * This method checks if a todo exists
     * @param uuid
     * @return boolean
     */
    public boolean todoExists(UUID uuid);

    /**
     * This method checks if a todo exists for a user
     * @param uuid
     * @param entityUser
     * @return boolean
     */
    public boolean isTodoEntityOwner(UUID uuid, EntityUser entityUser);

}

