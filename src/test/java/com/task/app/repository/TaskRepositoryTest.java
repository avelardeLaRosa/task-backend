package com.task.app.repository;

import com.task.app.TaskBackendApplication;
import com.task.app.entities.TaskEntity;
import com.task.app.entities.UserEntity;
import com.task.app.util.interfaces.IServiceConstants;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TaskBackendApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TaskRepositoryTest {

    @Autowired
    private ITaskRepository taskRepository;
    @Autowired
    private IUserRepository userRepository;
    private TaskEntity taskEntity;
    private UserEntity userEntity;

    @BeforeEach
    void init() {

        userEntity = UserEntity.builder()
                .id(1)
                .nombre("JUAN")
                .apellido("PEREZ")
                .cargo("Backend Developer")
                .dni("78945788")
                .telefono("987511023")
                .build();


        taskEntity = TaskEntity.builder()
                .id(1)
                .statusOperation(IServiceConstants.ACTIVE)
                .taskCode("TSK001")
                .descripcion("Tarea test con JUNIT")
                .fechaInicio(new Date())
                .fechaFin(new Date())
                .build();
    }

    @AfterEach
    void end() {
        this.taskRepository.deleteAll();
    }

    @DisplayName("TEST LISTAR")
    @Rollback
    @Test
    void list() {

        userEntity.setCreateDate(formatteDate("2023-12-21 00:00:00"));
        userEntity.setStatus(IServiceConstants.CREATED);
        userEntity.setUniqueIdentifier(UUID.randomUUID().toString());
        UserEntity userSaved = userRepository.save(userEntity);

        for (int i = 0; i <= 99; i++) {
            taskEntity.setCreateDate(formatteDate("2023-12-21 00:00:00"));
            taskEntity.setStatus(IServiceConstants.CREATED);
            taskEntity.setUniqueIdentifier(UUID.randomUUID().toString());
            taskEntity.setUserEntity(userSaved);
            taskRepository.save(taskEntity);
        }
        List<TaskEntity> taskEntities = this.taskRepository.findAll();
        if (taskEntities.isEmpty()) {
            log.error("LISTA DE TAREAS VACIA");
        }
        assertThat(taskEntities).hasSize(100);
        log.error("LISTA TAREAS ES : {}", taskEntities.size());
    }


    @DisplayName("TEST GUARDAR")
    @Rollback
    @Test
    void save() {

        userEntity.setCreateDate(formatteDate("2023-12-21 00:00:00"));
        userEntity.setStatus(IServiceConstants.CREATED);
        userEntity.setUniqueIdentifier(UUID.randomUUID().toString());
        UserEntity userSaved = userRepository.save(userEntity);


        taskEntity.setCreateDate(formatteDate("2023-12-21 00:00:00"));
        taskEntity.setStatus(IServiceConstants.CREATED);
        taskEntity.setUniqueIdentifier(UUID.randomUUID().toString());
        taskEntity.setUserEntity(userSaved);
        TaskEntity taskSaved = taskRepository.save(taskEntity);

        assertThat(taskSaved.getId()).isPositive();
        assertThat(taskSaved.getTaskCode()).isEqualTo("TSK001");
    }

    @DisplayName("TEST ACTUALIZAR")
    @Rollback
    @Test
    void update() {

        userEntity.setCreateDate(formatteDate("2023-12-21 00:00:00"));
        userEntity.setStatus(IServiceConstants.CREATED);
        userEntity.setUniqueIdentifier(UUID.randomUUID().toString());
        UserEntity userSaved = userRepository.save(userEntity);

        taskEntity.setCreateDate(formatteDate("2023-12-21 00:00:00"));
        taskEntity.setStatus(IServiceConstants.CREATED);
        taskEntity.setUniqueIdentifier(UUID.randomUUID().toString());
        taskEntity.setUserEntity(userSaved);
        TaskEntity taskSaved = taskRepository.save(taskEntity);

        log.info("Valor de descripcion al guardar: {}", taskSaved.getDescripcion());
        log.info("Valor de status op al guardar: {}", taskSaved.getStatusOperation());

        taskSaved.setDescripcion("Prueba test actualizacion");
        taskSaved.setStatusOperation(IServiceConstants.INACTIVE);

        log.info("Valor de descripcion al actualizar: {}", taskSaved.getDescripcion());
        log.info("Valor de status op al actualizar: {}", taskSaved.getStatusOperation());

        TaskEntity taskUpdated = taskRepository.save(taskSaved);

        assertThat(taskUpdated.getDescripcion()).isEqualTo("Prueba test actualizacion");
        assertThat(taskUpdated.getStatusOperation()).isEqualTo(IServiceConstants.INACTIVE);
    }

    @DisplayName("TEST OBTENER POR ID")
    @Test
    void get() {
        userEntity.setCreateDate(formatteDate("2023-12-21 00:00:00"));
        userEntity.setStatus(IServiceConstants.CREATED);
        userEntity.setUniqueIdentifier(UUID.randomUUID().toString());
        UserEntity userSaved = userRepository.save(userEntity);

        taskEntity.setCreateDate(formatteDate("2023-12-21 00:00:00"));
        taskEntity.setStatus(IServiceConstants.CREATED);
        taskEntity.setUniqueIdentifier(UUID.randomUUID().toString());
        taskEntity.setUserEntity(userSaved);
        TaskEntity taskSaved = taskRepository.save(taskEntity);
        Optional<TaskEntity> optionalTask = this.taskRepository.findByUniqueIdentifierAndStatus(
                taskSaved.getUniqueIdentifier(),
                IServiceConstants.CREATED
        );
        TaskEntity taskFound = new TaskEntity();
        if (optionalTask.isEmpty()) {
            log.error("NO SE CONTRO REGISTRO DE TAREA");
        } else {
            taskFound = optionalTask.get();
        }
        assertThat(taskFound).isNotNull();
        log.info("TASK ENCONTRADO: {}", taskFound);
        assertThat(taskFound.getId()).isPositive();
        log.info("TASK ID: {}", taskFound.getId());
    }

    @DisplayName("TEST ELIMINACION")
    @Rollback
    @Test
    void delete() {
        userEntity.setCreateDate(formatteDate("2023-12-21 00:00:00"));
        userEntity.setStatus(IServiceConstants.CREATED);
        userEntity.setUniqueIdentifier(UUID.randomUUID().toString());
        UserEntity userSaved = userRepository.save(userEntity);

        taskEntity.setCreateDate(formatteDate("2023-12-21 00:00:00"));
        taskEntity.setStatus(IServiceConstants.CREATED);
        taskEntity.setUniqueIdentifier(UUID.randomUUID().toString());
        taskEntity.setUserEntity(userSaved);
        TaskEntity taskSaved = taskRepository.save(taskEntity);

        log.info("STATUS AL GUARDAR: {}", taskSaved.getStatus());

        taskSaved.setStatus(IServiceConstants.DELETED);

        log.info("STATUS AL ELIMINAR: {}", taskSaved.getStatus());

        TaskEntity taskUpdated = taskRepository.save(taskSaved);

        assertThat(taskUpdated.getStatus()).isEqualTo(IServiceConstants.DELETED);
    }

    private LocalDateTime formatteDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(date, formatter);
    }
}
