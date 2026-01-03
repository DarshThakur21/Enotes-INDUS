package com.enotes.Enotes_INDUS.endpoints;

import com.enotes.Enotes_INDUS.dto.TodoDto;
import com.enotes.Enotes_INDUS.model.enums.Status;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.enotes.Enotes_INDUS.utils.Constants.ROLE_USER;

@RequestMapping("/api/v1/todo")
public interface TodoEndpoint {

    @GetMapping("/all")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> getAllTodos();

    @PostMapping("/save-todo")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> saveTodos(@RequestBody TodoDto todoDto);


    @GetMapping("/{id}")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> getTodoById(@PathVariable Integer id);

    @GetMapping("/by-status/{status}")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> getTodoByStatus(@PathVariable String status);

    @DeleteMapping("/delete/{todoId}")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> deleteTodo(@PathVariable Integer todoId);

    @PutMapping("/{id}/status")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> changeStatus(@PathVariable Integer id,@RequestBody Status status);

}
