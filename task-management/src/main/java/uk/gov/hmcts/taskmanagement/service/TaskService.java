package uk.gov.hmcts.taskmanagement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.taskmanagement.models.Status;
import uk.gov.hmcts.taskmanagement.models.Task;
import uk.gov.hmcts.taskmanagement.models.dto.TaskRequestDTO;
import uk.gov.hmcts.taskmanagement.models.dto.TaskResponseDTO;
import uk.gov.hmcts.taskmanagement.repo.TaskRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

  private final TaskRepository taskRepository;

  public TaskResponseDTO createTask(TaskRequestDTO taskRequestDTO){
    return mapToResponseDTO(taskRepository.save(mapToTaskEntity(taskRequestDTO)));
  }

  public List<TaskResponseDTO> getAllTasks(){
    return taskRepository.findAll()
        .stream()
        .map(this::mapToResponseDTO)
        .toList();
  }

  public TaskResponseDTO getTaskById(Long id){
    Task task = checkIfTaskExists(id);

    return mapToResponseDTO(task);
  }

  public void deleteTask(Long id){
    Task task = checkIfTaskExists(id);

    taskRepository.delete(task);
  }

  public TaskResponseDTO updateTask(TaskRequestDTO taskRequestDTO, Long id){
    Task task = checkIfTaskExists(id);

    task.setTitle(taskRequestDTO.getTitle());
    task.setDescription(taskRequestDTO.getDescription());
    task.setStatus(taskRequestDTO.getStatus());
    task.setDueDate(taskRequestDTO.getDueDate());

    return mapToResponseDTO(taskRepository.save(task));
  }

  private Task checkIfTaskExists(Long id){
    return taskRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Task not found"));
  }

  private Task mapToTaskEntity(TaskRequestDTO taskRequestDTO){
    return Task.builder()
        .title(taskRequestDTO.getTitle())
        .description(taskRequestDTO.getDescription())
        .dueDate(taskRequestDTO.getDueDate())
        .status(taskRequestDTO.getStatus() != null ? taskRequestDTO.getStatus() : Status.TODO)
        .build();
  }

  private TaskResponseDTO mapToResponseDTO(Task task){
    return TaskResponseDTO.builder()
        .id(task.getId())
        .title(task.getTitle())
        .description(task.getDescription())
        .dueDate(task.getDueDate())
        .status(task.getStatus())
        .build();
  }
}
