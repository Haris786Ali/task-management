package uk.gov.hmcts.taskmanagement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.taskmanagement.models.dto.TaskRequestDTO;
import uk.gov.hmcts.taskmanagement.models.dto.TaskResponseDTO;
import uk.gov.hmcts.taskmanagement.service.TaskService;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RequestMapping("/tasks")
@RestController
@RequiredArgsConstructor
public class TaskController {

  private final TaskService taskService;

  @GetMapping()
  public ResponseEntity<List<TaskResponseDTO>> getAllTasks(){
    return ResponseEntity.ok(taskService.getAllTasks());
  }

  @GetMapping("/{id}")
  public ResponseEntity<TaskResponseDTO> getTasksById(@PathVariable Long id){
    return ResponseEntity.ok(taskService.getTaskById(id));
  }

  @PostMapping
  public ResponseEntity<TaskResponseDTO> createNewTask(@Valid @RequestBody TaskRequestDTO taskRequestDTO){
    return ResponseEntity.status(CREATED).body(taskService.createTask(taskRequestDTO));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTask(@PathVariable Long id){
    taskService.deleteTask(id);
    return ResponseEntity.noContent().build();

  }

  @PatchMapping("/{id}")
  public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable Long id, @Valid @RequestBody TaskRequestDTO taskRequestDTO){
    return ResponseEntity.ok(taskService.updateTask(taskRequestDTO, id));
  }
}

