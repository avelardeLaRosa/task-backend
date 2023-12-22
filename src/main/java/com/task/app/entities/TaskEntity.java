package com.task.app.entities;


import com.task.app.dto.TaskDTO;
import com.task.app.util.entities.AuditoryEntity;
import com.task.app.util.interfaces.IDTOManagment;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Table(name = "t_task")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskEntity extends AuditoryEntity implements IDTOManagment<TaskDTO> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "tx_status_operation")
    private String statusOperation;
    @Column(name = "tx_task_code")
    private String taskCode;
    @Column(name = "tx_descripcion")
    private String descripcion;
    @Column(name = "tx_fecha_inicio")
    private Date fechaInicio;
    @Column(name = "tx_fecha_fin")
    private Date fechaFin;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    private UserEntity userEntity;

    @Override
    public TaskDTO getDTO() {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(getUniqueIdentifier());
        taskDTO.setStatus(getStatus());
        taskDTO.setCreateUser(getCreateUser());
        taskDTO.setUpdateUser(getUpdateUser());
        taskDTO.setDeleteUser(getDeleteUser());
        taskDTO.setCreateDate(getCreateDate());
        taskDTO.setUpdateDate(getUpdateDate());
        taskDTO.setDeleteDate(getDeleteDate());

        taskDTO.setTaskCode(this.taskCode);
        taskDTO.setDescripcion(this.descripcion);
        taskDTO.setFechaInicio(this.fechaInicio);
        taskDTO.setFechaFin(this.fechaFin);
        taskDTO.setUserDTO(this.userEntity.getDTO());
        return taskDTO;
    }

    @Override
    public void setEntity(TaskDTO taskDTO) {
        setStatus(taskDTO.getStatus());
        setUniqueIdentifier(taskDTO.getId());
        setCreateUser(taskDTO.getCreateUser());
        setUpdateUser(taskDTO.getUpdateUser());
        setDeleteUser(taskDTO.getDeleteUser());
        setCreateDate(taskDTO.getCreateDate());
        setUpdateDate(taskDTO.getUpdateDate());
        setDeleteDate(taskDTO.getDeleteDate());

        this.taskCode = taskDTO.getTaskCode();
        this.descripcion = taskDTO.getDescripcion();
        this.fechaInicio = taskDTO.getFechaInicio();
        this.fechaFin = taskDTO.getFechaFin();
    }
}
