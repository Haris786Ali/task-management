package uk.gov.hmcts.taskmanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.taskmanagement.models.dto.TaskRequestDTO;
import uk.gov.hmcts.taskmanagement.models.dto.TaskResponseDTO;
import uk.gov.hmcts.taskmanagement.service.TaskService;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RequestMapping("/tasks")
@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "Task management endpoints")
public class TaskController {

  private final TaskService taskService;

  @GetMapping()
  @Operation(summary = "Get all tasks", description = "Returns a list of all tasks")
  public ResponseEntity<List<TaskResponseDTO>> getAllTasks(){
    log.info("Received request for all tasks");
    return ResponseEntity.ok(taskService.getAllTasks());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get a singular task by ID",
      description = "Returns one task based on the ID provided and if found, or code 404 if the task is not found")
  public ResponseEntity<TaskResponseDTO> getTasksById(@PathVariable Long id){
    log.info("Received request for task id: {}", id);
    return ResponseEntity.ok(taskService.getTaskById(id));
  }

  @PostMapping
  @Operation(summary = "Creates new task",
      description = "Returns the newly created object based on the provided taskRequestDTO")
  public ResponseEntity<TaskResponseDTO> createNewTask(@Valid @RequestBody TaskRequestDTO taskRequestDTO){
    log.info("Received request for new task creation: {}", taskRequestDTO);
    return ResponseEntity.status(CREATED).body(taskService.createTask(taskRequestDTO));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a task by ID",
      description = "Returns No content for entity deletion, or code 404 if the task is not found")
  public ResponseEntity<Void> deleteTask(@PathVariable Long id){
    log.info("Received request for task deletion, id: {}", id);
    taskService.deleteTask(id);
    return ResponseEntity.noContent().build();

  }

  @PatchMapping("/{id}")
  @Operation(summary = "Update a task", description = "Returns an updated task based on the updated taskRequesDTO 200, or code 404 if the task is not found")
  public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable Long id, @Valid @RequestBody TaskRequestDTO taskRequestDTO){
    log.info("Received request for updated task information, id: {}", id);
    return ResponseEntity.ok(taskService.updateTask(taskRequestDTO, id));
  }
}

