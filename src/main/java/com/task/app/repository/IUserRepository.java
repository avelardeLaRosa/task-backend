package com.task.app.repository;

import com.task.app.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<UserEntity, Integer> {

    Optional<UserEntity> findByDniAndStatus(String dni, String status);

    Optional<UserEntity> findByUniqueIdentifierAndStatus(String id, String status);

    @Query(
            value = "SELECT u FROM UserEntity  u " +
                    "where u.status = :status " +
                    "and ( " +
                    "(u.dni like concat('%', :filter, '%')) or " +
                    "(u.nombre like concat('%', :filter, '%')) or " +
                    "(u.apellido like concat('%', :filter, '%'))" +
                    ") " +
                    "and (:cargo is null OR u.cargo = :cargo) "
    )
    Page<UserEntity> getUserEntities(
            String status,
            String filter,
            String cargo,
            Pageable pageable
    );
}
