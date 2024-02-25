package engineer.cafisodevs.todoapp.todo.controller;

import engineer.cafisodevs.todoapp.account.services.AccountServiceImpl;
import engineer.cafisodevs.todoapp.todo.TodoEntity;
import engineer.cafisodevs.todoapp.todo.dto.TodoDTO;
import engineer.cafisodevs.todoapp.todo.service.TodoServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/todo")
@RequiredArgsConstructor
public class TodoController {


    private final TodoServiceImpl todoService;
    private final AccountServiceImpl accountService;


    @PostMapping("/create")
    public ResponseEntity<Object> createTodo(HttpServletRequest request, @RequestBody TodoDTO todoDTO) {
        try {
            todoDTO.setCreatedBy(accountService.loadAccount(request.getUserPrincipal()).orElseThrow(() -> new RuntimeException("Error loading account")).getUuid());

            if (todoService.createTodoEntity(todoDTO.toEntity(accountService)).isPresent()) {
                return ResponseEntity.ok().body("Todo created successfully");
            } else {
                throw new RuntimeException("Error creating todo"); // this will be caught by the catch block
            }
        } catch (Exception e) {
            System.out.println("Exception caught: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/load/all")
    public ResponseEntity<Object> loadAll(HttpServletRequest request) {
        try {
            List<TodoDTO> allTodos = new ArrayList<>();

            todoService.loadAllTodoEntitiesbyUser(accountService.loadAccount(request.getUserPrincipal()).orElseThrow(() -> new RuntimeException("Error loading account"))).forEach(todoEntity -> {
                allTodos.add(TodoDTO.builder()
                        .name(todoEntity.getName())
                        .detail(todoEntity.getDetail())
                        .createdAt(todoEntity.getCreatedAt().toString())
                        .createdBy(todoEntity.getCreatedBy().getUuid())
                        .build());
            });

            return ResponseEntity.ok().body(allTodos);


        } catch (Exception e) {
            System.out.println("Exception caught: ");
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/update")
    public ResponseEntity<Object> update(HttpServletRequest request, @RequestBody TodoDTO todoDTO) {
        try {

            if (todoDTO.getCreatedBy() == null) {
                todoDTO.setCreatedBy(accountService.loadAccount(request.getUserPrincipal()).orElseThrow(() -> new RuntimeException("Error loading account")).getUuid());
            }

            if (todoService.updateTodoEntity(todoDTO).isPresent()) {
                return ResponseEntity.ok().body("Todo updated successfully");
            } else {
                throw new RuntimeException("Error updating todo"); // this will be caught by the catch block
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/load/{uuid}")
    public ResponseEntity<Object> load(HttpServletRequest request, @PathVariable String uuid) {
        try {
            return ResponseEntity.ok().body(todoService.loadTodoEntity(java.util.UUID.fromString(uuid)).orElseThrow(() -> new RuntimeException("Error loading todo")));
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }


    @PostMapping("/delete/{uuid}")

    public ResponseEntity<Object> delete(HttpServletRequest request, @PathVariable String uuid) {
        try {
            todoService.deleteTodoEntity(java.util.UUID.fromString(uuid));
            return ResponseEntity.ok().body("Todo deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
