package uk.gov.hmcts.taskmanagement.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;
import uk.gov.hmcts.taskmanagement.exception.TaskNotFoundException;
import uk.gov.hmcts.taskmanagement.models.dto.TaskRequestDTO;
import uk.gov.hmcts.taskmanagement.models.dto.TaskResponseDTO;
import uk.gov.hmcts.taskmanagement.service.TaskService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.gov.hmcts.taskmanagement.models.Status.TODO;

@ExtendWith(MockitoExtension.class)
@WebMvcTest
public class TaskControllerTest {

  private static final String PATH = "/tasks";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private TaskService taskService;

  @Test
  void getAllTasks_Returns200() throws Exception {
    List<TaskResponseDTO> listOfResponses = new ArrayList<>();
    listOfResponses.add(createTaskResponseDTO(1L));
    listOfResponses.add(createTaskResponseDTO(2L));

    when(taskService.getAllTasks()).thenReturn(listOfResponses);

    mockMvc.perform(get(PATH).contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(listOfResponses)));
  }

  @Test
  void getTasksById_Returns200() throws Exception {
    TaskResponseDTO expectedResult = createTaskResponseDTO(1L);
    when(taskService.getTaskById(1L)).thenReturn(expectedResult);

    mockMvc.perform(get(PATH + "/1").contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(expectedResult)));
  }

  @Test
  void getTasksById_Returns404_TaskNotFound() throws Exception {
    when(taskService.getTaskById(1L)).thenThrow(new TaskNotFoundException("Task not found"));

    mockMvc.perform(get(PATH + "/1").contentType(APPLICATION_JSON))
        .andExpect(content().string(containsString("Task not found")))
        .andExpect(status().isNotFound());
  }

  @Test
  void createTask_Returns201() throws Exception {
    TaskResponseDTO expectedTaskResponseDTO = createTaskResponseDTO(1L);
    TaskRequestDTO taskRequestDTO = createTaskRequestDTO();
    when(taskService.createTask(taskRequestDTO)).thenReturn(expectedTaskResponseDTO);

    mockMvc.perform(post(PATH).contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(taskRequestDTO)))
        .andExpect(content().string(objectMapper.writeValueAsString(expectedTaskResponseDTO)))
        .andExpect(status().isCreated());
  }

  @Test
  void deleteTask_Returns204() throws Exception {
    mockMvc.perform(delete(PATH + "/1"))
        .andExpect(status().isNoContent());

    verify(taskService, times(1)).deleteTask(1L);
  }

  @Test
  void deleteTask_Returns404_TaskNotFound() throws Exception {
    doThrow(new TaskNotFoundException("Task not found")).when(taskService).deleteTask(1L);

    mockMvc.perform(delete(PATH + "/1").contentType(APPLICATION_JSON))
        .andExpect(content().string(containsString("Task not found")))
        .andExpect(status().isNotFound());
  }

  @Test
  void updateTask_Returns200() throws Exception{
    TaskResponseDTO expectedTaskResponseDTO = createTaskResponseDTO(1L);
    TaskRequestDTO taskRequestDTO = createTaskRequestDTO();
    when(taskService.updateTask(taskRequestDTO, 1L)).thenReturn(expectedTaskResponseDTO);

    mockMvc.perform(patch(PATH + "/1").contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(taskRequestDTO)))
        .andExpect(content().string(objectMapper.writeValueAsString(expectedTaskResponseDTO)))
        .andExpect(status().isOk());
  }

  @Test
  void updateTask_Returns404_TaskNotFound() throws Exception{
    TaskRequestDTO taskRequestDTO = createTaskRequestDTO();
    when(taskService.updateTask(taskRequestDTO,1L)).thenThrow(new TaskNotFoundException("Task not found"));

    mockMvc.perform(patch(PATH + "/1").contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(taskRequestDTO)))
        .andExpect(content().string(containsString("Task not found")))
        .andExpect(status().isNotFound());
  }

  private TaskResponseDTO createTaskResponseDTO(Long id){
    return TaskResponseDTO.builder()
        .id(id)
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
