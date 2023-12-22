package com.task.app.service;

import com.task.app.dto.TaskDTO;
import com.task.app.dto.UserDTO;
import com.task.app.entities.TaskEntity;
import com.task.app.entities.UserEntity;
import com.task.app.repository.ITaskRepository;
import com.task.app.repository.IUserRepository;
import com.task.app.service.impl.TaskServiceImpl;
import com.task.app.service.impl.UserServiceImpl;
import com.task.app.util.ApiResponse;
import com.task.app.util.Response;
import com.task.app.util.interfaces.IServiceConstants;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
class TaskServiceImplTest {
    @Mock
    private ITaskRepository taskRepository;
    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private TaskServiceImpl taskService;
    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void end() throws Exception {
        MockitoAnnotations.openMocks(this).close();
    }

    @DisplayName("TEST GUARDAR SUCCESS")
    @Test
    void saveSucess() {

        UserEntity userEntity = this.getUserEntity();
        userEntity.setCreateDate(formatteDate("2023-12-21 00:00:00"));
        userEntity.setStatus(IServiceConstants.CREATED);
        userEntity.setUniqueIdentifier(UUID.randomUUID().toString());
        log.info("UQ A GUARDADO: {}", userEntity.getUniqueIdentifier());

        given(this.userRepository.findByDniAndStatus(userEntity.getDni(), IServiceConstants.CREATED))
                .willReturn(Optional.empty());

        given(this.userRepository.save(any(UserEntity.class)))
                .willReturn(userEntity);

        ApiResponse<Response<UserDTO>> userSaved = this.userService.add(userEntity.getDTO());
        assertThat(userSaved.getExitoso()).isTrue();

        log.info("USUARIO GUARDADO: {}", userSaved.getData().getT().getId());

        UserEntity userEntitySaved = new UserEntity();
        userEntitySaved.setEntity(userSaved.getData().getT());


        TaskEntity taskEntity = this.getTaskEntity();
        taskEntity.setCreateDate(formatteDate("2023-12-21 00:00:00"));
        taskEntity.setStatus(IServiceConstants.CREATED);
        taskEntity.setUniqueIdentifier(UUID.randomUUID().toString());
        taskEntity.setUserEntity(userEntitySaved);

        given(this.userRepository.findByUniqueIdentifierAndStatus(taskEntity.getUserEntity().getUniqueIdentifier(), IServiceConstants.CREATED))
                .willReturn(Optional.of(userEntity));

        given(this.taskRepository.save(any(TaskEntity.class)))
                .willReturn(taskEntity);

        ApiResponse<Response<TaskDTO>> taskSaved = this.taskService.add(taskEntity.getDTO());
        assertThat(taskSaved.getExitoso()).isTrue();
    }

    @DisplayName("TEST ACTUALIZAR SUCCESS")
    @Test
    void updateSuccess() {
        UserEntity userEntity = this.getUserEntity();
        userEntity.setCreateDate(formatteDate("2023-12-21 00:00:00"));
        userEntity.setStatus(IServiceConstants.CREATED);
        userEntity.setUniqueIdentifier(UUID.randomUUID().toString());
        log.info("UQ A GUARDADO: {}", userEntity.getUniqueIdentifier());

        given(this.userRepository.findByDniAndStatus(userEntity.getDni(), IServiceConstants.CREATED))
                .willReturn(Optional.empty());

        given(this.userRepository.save(any(UserEntity.class)))
                .willReturn(userEntity);

        ApiResponse<Response<UserDTO>> userSaved = this.userService.add(userEntity.getDTO());
        assertThat(userSaved.getExitoso()).isTrue();

        log.info("USUARIO GUARDADO: {}", userSaved.getData().getT().getId());

        UserEntity userEntitySaved = new UserEntity();
        userEntitySaved.setEntity(userSaved.getData().getT());

        TaskEntity taskEntity = this.getTaskEntity();
        taskEntity.setCreateDate(formatteDate("2023-12-21 00:00:00"));
        taskEntity.setStatus(IServiceConstants.CREATED);
        taskEntity.setUniqueIdentifier(UUID.randomUUID().toString());
        taskEntity.setUserEntity(userEntitySaved);

        log.info("uq : {}", taskEntity.getUserEntity().getUniqueIdentifier());

        given(this.userRepository.findByUniqueIdentifierAndStatus(taskEntity.getUserEntity().getUniqueIdentifier(), IServiceConstants.CREATED))
                .willReturn(Optional.of(userEntity));

        given(this.taskRepository.save(any(TaskEntity.class)))
                .willReturn(taskEntity);

        ApiResponse<Response<TaskDTO>> taskSaved = this.taskService.add(taskEntity.getDTO());
        log.info("task response: {}", taskSaved);
        assertThat(taskSaved.getExitoso()).isTrue();


        given(this.userRepository.findByUniqueIdentifierAndStatus(taskSaved.getData().getT().getId(), IServiceConstants.CREATED))
                .willReturn(Optional.of(userEntity));
        given(this.taskRepository.findByUniqueIdentifierAndStatusOperationNotLike(taskSaved.getData().getT().getId(), IServiceConstants.CLOSED))
                .willReturn(Optional.of(taskEntity));

        TaskEntity taskUpdated = new TaskEntity();
        taskUpdated.setEntity(taskSaved.getData().getT());
        taskUpdated.setDescripcion("Prueba actualizacion");
        taskUpdated.setUserEntity(userEntitySaved);

        given(this.taskRepository.save(any(TaskEntity.class)))
                .willReturn(taskUpdated);

        ApiResponse<Response<TaskDTO>> taskUpdate = this.taskService.update(taskUpdated.getDTO());
        log.info("DESCRIPCION ACTUALIZADO: {}", taskUpdate.getData().getT().getDescripcion());

        assertThat(taskUpdate.getExitoso()).isTrue();
        assertThat(taskUpdate.getData().getT().getDescripcion()).isEqualTo("Prueba actualizacion");
    }

    @DisplayName("TEST OBTENER POR ID SUCCESS")
    @Test
    void getSuccess() {

        UserEntity userEntity = this.getUserEntity();
        userEntity.setCreateDate(formatteDate("2023-12-21 00:00:00"));
        userEntity.setStatus(IServiceConstants.CREATED);
        userEntity.setUniqueIdentifier(UUID.randomUUID().toString());

        given(this.userRepository.findByDniAndStatus(userEntity.getDni(), IServiceConstants.CREATED))
                .willReturn(Optional.empty());

        given(this.userRepository.save(any(UserEntity.class)))
                .willReturn(userEntity);

        ApiResponse<Response<UserDTO>> userSaved = this.userService.add(userEntity.getDTO());
        assertThat(userSaved.getExitoso()).isTrue();


        UserEntity userEntitySaved = new UserEntity();
        userEntitySaved.setEntity(userSaved.getData().getT());


        TaskEntity taskEntity = this.getTaskEntity();
        taskEntity.setCreateDate(formatteDate("2023-12-21 00:00:00"));
        taskEntity.setStatus(IServiceConstants.CREATED);
        taskEntity.setUniqueIdentifier(UUID.randomUUID().toString());
        taskEntity.setUserEntity(userEntitySaved);

        given(this.userRepository.findByUniqueIdentifierAndStatus(taskEntity.getUserEntity().getUniqueIdentifier(), IServiceConstants.CREATED))
                .willReturn(Optional.of(userEntity));

        given(this.taskRepository.save(any(TaskEntity.class)))
                .willReturn(taskEntity);

        ApiResponse<Response<TaskDTO>> taskSaved = this.taskService.add(taskEntity.getDTO());
        assertThat(taskSaved.getExitoso()).isTrue();


        given(this.taskRepository.findByUniqueIdentifierAndStatus(taskSaved.getData().getT().getId(), IServiceConstants.CREATED))
                .willReturn(Optional.of(taskEntity));

        ApiResponse<Response<TaskDTO>> taskFound = this.taskService.get(taskEntity.getUniqueIdentifier());

        assertThat(taskFound.getExitoso()).isTrue();
        assertThat(taskFound.getData().getT().getId()).isNotNull();
        log.info("TAREA ENCONTRADA CON CODIGO: {}", taskFound.getData().getT().getTaskCode());

    }

    TaskEntity getTaskEntity() {
        return TaskEntity.builder()
                .id(1)
                .statusOperation(IServiceConstants.ACTIVE)
                .taskCode("TSK001")
                .descripcion("Tarea test con JUNIT")
                .fechaInicio(new Date())
                .fechaFin(new Date())
                .build();
    }

    UserEntity getUserEntity() {
        return UserEntity.builder()
                .id(1)
                .nombre("JUAN")
                .apellido("PEREZ")
                .cargo("Backend Developer")
                .dni("78945788")
                .telefono("987511023")
                .build();
    }

    private LocalDateTime formatteDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(date, formatter);
    }
}
