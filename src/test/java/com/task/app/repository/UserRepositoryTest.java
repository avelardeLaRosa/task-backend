package com.task.app.repository;

import com.task.app.TaskBackendApplication;
import com.task.app.entities.UserEntity;
import com.task.app.util.IServiceConstants;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TaskBackendApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    @Autowired
    private IUserRepository userRepository;
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
    }

    @AfterEach
    void end() {
        this.userRepository.deleteAll();
    }

    @DisplayName("TEST LISTAR")
    @Rollback
    @Test
    void list() {

        for (int i = 0; i <= 99; i++) {
            userEntity.setCreateDate(formatteDate("2023-12-21 00:00:00"));
            userEntity.setStatus(IServiceConstants.CREATED);
            userEntity.setUniqueIdentifier(UUID.randomUUID().toString());
            userRepository.save(userEntity);
        }
        List<UserEntity> userEntities = this.userRepository.findAll();
        if (userEntities.isEmpty()) {
            log.error("LISTA DE TAREAS VACIA");
        }
        assertThat(userEntities).hasSize(100);
        log.error("LISTA TAREAS ES : {}", userEntities.size());
    }


    @DisplayName("TEST GUARDAR")
    @Rollback
    @Test
    void save() {

        userEntity.setCreateDate(formatteDate("2023-12-21 00:00:00"));
        userEntity.setStatus(IServiceConstants.CREATED);
        userEntity.setUniqueIdentifier(UUID.randomUUID().toString());
        UserEntity userSaved = userRepository.save(userEntity);

        assertThat(userSaved.getId()).isPositive();
        assertThat(userSaved.getDni()).isEqualTo("78945788");
    }

    @DisplayName("TEST ACTUALIZAR")
    @Rollback
    @Test
    void update() {

        userEntity.setCreateDate(formatteDate("2023-12-21 00:00:00"));
        userEntity.setStatus(IServiceConstants.CREATED);
        userEntity.setUniqueIdentifier(UUID.randomUUID().toString());
        UserEntity userSaved = userRepository.save(userEntity);

        log.info("Valor de DNI al guardar: {}", userSaved.getDni());
        log.info("Valor de NOMBRE op al guardar: {}", userSaved.getNombre());

        userSaved.setDni("12345678");
        userSaved.setNombre("alexander");

        log.info("Valor de DNI al actualizar: {}", userSaved.getDni());
        log.info("Valor de NOMBRE op al actualizar: {}", userSaved.getNombre());

        UserEntity userUpdated = this.userRepository.save(userSaved);

        assertThat(userUpdated.getDni()).isEqualTo("12345678");
        assertThat(userUpdated.getNombre()).isEqualTo("alexander");
    }

    @DisplayName("TEST OBTENER POR ID")
    @Test
    void get() {
        userEntity.setCreateDate(formatteDate("2023-12-21 00:00:00"));
        userEntity.setStatus(IServiceConstants.CREATED);
        userEntity.setUniqueIdentifier(UUID.randomUUID().toString());
        UserEntity userSaved = userRepository.save(userEntity);

        Optional<UserEntity> optionalUserEntity = this.userRepository.findByUniqueIdentifierAndStatus(
                userSaved.getUniqueIdentifier(),
                IServiceConstants.CREATED
        );
        UserEntity userFound = new UserEntity();
        if (optionalUserEntity.isEmpty()) {
            log.error("NO SE CONTRO REGISTRO DE TAREA");
        } else {
            userFound = optionalUserEntity.get();
        }
        assertThat(userFound).isNotNull();
        log.info("TASK ENCONTRADO: {}", userFound);
        assertThat(userFound.getId()).isPositive();
        log.info("TASK ID: {}", userFound.getId());
    }

    @DisplayName("TEST ELIMINACION")
    @Rollback
    @Test
    void delete() {
        userEntity.setCreateDate(formatteDate("2023-12-21 00:00:00"));
        userEntity.setStatus(IServiceConstants.CREATED);
        userEntity.setUniqueIdentifier(UUID.randomUUID().toString());
        UserEntity userSaved = userRepository.save(userEntity);

        log.info("STATUS AL GUARDAR: {}", userSaved.getStatus());

        userSaved.setStatus(IServiceConstants.DELETED);

        log.info("STATUS AL ELIMINAR: {}", userSaved.getStatus());

        UserEntity userDeleted = this.userRepository.save(userSaved);

        assertThat(userDeleted.getStatus()).isEqualTo(IServiceConstants.DELETED);
    }

    private LocalDateTime formatteDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(date, formatter);
    }
}
