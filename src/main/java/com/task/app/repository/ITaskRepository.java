package com.task.app.repository;

import com.task.app.entities.TaskEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface ITaskRepository extends JpaRepository<TaskEntity, Integer> {

    Optional<TaskEntity> findByUniqueIdentifierAndStatusOperationNotLike(String id, String status);

    Optional<TaskEntity> findByUniqueIdentifierAndStatus(String id, String status);

    @Query(
            value = "SELECT t FROM TaskEntity t " +
                    "where t.status = :status " +
                    "and (t.fechaInicio >= :fechaInicio or :fechaInicio is null) " +
                    "and (t.fechaFin >= :fechaFin or :fechaFin is null) " +
                    "and (t.statusOperation = :statusOp or :statusOp is null) "
    )
    Page<TaskEntity> getTaskEntities(
            String status,
            Date fechaInicio,
            Date fechaFin,
            String statusOp,
            Pageable pageable
    );
}
