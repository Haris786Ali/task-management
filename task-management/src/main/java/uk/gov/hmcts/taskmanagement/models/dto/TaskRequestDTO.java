package uk.gov.hmcts.taskmanagement.models.dto;

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

  private String title;

  private String description;

  private Status status;

  private LocalDateTime dueDate;
}
