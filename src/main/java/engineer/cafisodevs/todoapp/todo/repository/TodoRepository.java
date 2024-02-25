package engineer.cafisodevs.todoapp.todo.repository;

import engineer.cafisodevs.todoapp.todo.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, UUID> {




    @Query("SELECT t FROM TodoEntity t JOIN t.createdBy a WHERE a.email = ?1")
    List<TodoEntity> findAllByEntityUserEmail(String email);



    @Query("SELECT t FROM TodoEntity t JOIN t.createdBy a WHERE a.uuid = ?1")
    List<TodoEntity> findAllByEntityUserUUID(UUID uuid);

}
