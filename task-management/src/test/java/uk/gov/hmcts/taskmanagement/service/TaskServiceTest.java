package uk.gov.hmcts.taskmanagement.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.taskmanagement.models.Task;
import uk.gov.hmcts.taskmanagement.models.dto.TaskRequestDTO;
import uk.gov.hmcts.taskmanagement.models.dto.TaskResponseDTO;
import uk.gov.hmcts.taskmanagement.repo.TaskRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.taskmanagement.models.Status.DONE;
import static uk.gov.hmcts.taskmanagement.models.Status.TODO;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

  @Mock
  private TaskRepository taskRepository;

  @InjectMocks
  private TaskService taskService;

  @Test
  void createTaskTest(){
    Task expectedTask = createTaskEntity(1L);

    when(taskRepository.save(any(Task.class))).thenReturn(expectedTask);

    TaskResponseDTO actualTaskResponseDTO = taskService.createTask(createTaskRequestDTO());

    assertEquals(expectedTask.getId(), actualTaskResponseDTO.getId());
    assertEquals(expectedTask.getTitle(), actualTaskResponseDTO.getTitle());
    assertEquals(expectedTask.getDescription(), actualTaskResponseDTO.getDescription());
    assertEquals(expectedTask.getDueDate(), actualTaskResponseDTO.getDueDate());
    assertEquals(expectedTask.getStatus(), actualTaskResponseDTO.getStatus());
  }

  @Test
  void getAllTasksTest() {
    when(taskRepository.findAll()).thenReturn(Collections.singletonList(createTaskEntity()));

    List<TaskResponseDTO> actualAnswers = taskService.getAllTasks();

    assertEquals(1, actualAnswers.size());
    assertEquals("Example title", actualAnswers.getFirst().getTitle());
  }

  @Test
  void getTaskByIdTest() {
    when(taskRepository.findById(1L)).thenReturn(Optional.of(createTaskEntity()));

    TaskResponseDTO actualTaskResponseDTO = taskService.getTaskById(1L);
    assertEquals("Example title", actualTaskResponseDTO.getTitle());
  }

  @Test
  void getTaskByIdThrowExceptionTest() {
    when(taskRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> taskService.getTaskById(1L));
  }

  @Test
  void deleteTaskTest() {
    when(taskRepository.findById(1L)).thenReturn(Optional.of(createTaskEntity()));

    taskService.deleteTask(1L);
    verify(taskRepository).delete(createTaskEntity());
  }

  @Test
  void deleteTaskThrowExceptionTest() {
    when(taskRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> taskService.deleteTask(1L));
  }

  @Test
  void updateTaskTest() {
    Task expectedTask = createTaskEntity(1L);
    expectedTask.setTitle("New Title");
    expectedTask.setDescription("New Description");
    expectedTask.setStatus(DONE);

    when(taskRepository.findById(1L)).thenReturn(Optional.of(createTaskEntity(1L)));
    when(taskRepository.save(any(Task.class))).thenReturn(expectedTask);

    TaskRequestDTO taskRequestDTO = createTaskRequestDTO();
    taskRequestDTO.setTitle("New Title");
    taskRequestDTO.setDescription("New Description");
    taskRequestDTO.setStatus(DONE);

    TaskResponseDTO actualTaskResponseDTO = taskService.updateTask(taskRequestDTO, 1L);

    assertEquals(expectedTask.getTitle(), actualTaskResponseDTO.getTitle());
    assertEquals(expectedTask.getDescription(), actualTaskResponseDTO.getDescription());
    assertEquals(expectedTask.getStatus(), actualTaskResponseDTO.getStatus());
  }

  @Test
  void updateTaskThrowExceptionTest() {
    when(taskRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> taskService.updateTask(createTaskRequestDTO(), 1L));
  }

  private Task createTaskEntity(Long id){
    Task task = createTaskEntity();
    task.setId(id);

    return task;
  }

  private Task createTaskEntity(){
    return Task.builder()
        .title("Example title")
        .description("Example Description")
        .dueDate(LocalDateTime.of(2026, 7, 12, 14, 15))
        .status(TODO)
        .build();
  }

  private TaskRequestDTO createTaskRequestDTO(){
    return TaskRequestDTO.builder()
        .title("Example title")
        .description("Example Description")
        .dueDate(LocalDateTime.of(2026, 7, 12, 14, 15))
        .status(TODO)
        .build();
  }

}
