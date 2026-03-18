package uk.gov.hmcts.taskmanagement.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.taskmanagement.models.Status;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskRequestDTO {

  @NotBlank
  private String title;
  private String description;

  @NotNull
  private Status status;
  private LocalDateTime dueDate;
}
