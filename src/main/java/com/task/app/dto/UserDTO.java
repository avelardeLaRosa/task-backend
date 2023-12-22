package com.task.app.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.task.app.util.dto.AuditoryDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO extends AuditoryDTO {
    private String id;
    private String nombre;
    private String apellido;
    private String cargo;
    private String dni;
    private String telefono;
    private List<TaskDTO> taskDTOS;
}
