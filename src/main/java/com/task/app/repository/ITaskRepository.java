package com.task.app.repository;

import com.task.app.entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ITaskRepository extends JpaRepository<TaskEntity, Integer> {

    Optional<TaskEntity> findByUniqueIdentifierAndStatusOperationNotLike(String id, String status);
    Optional<TaskEntity> findByUniqueIdentifierAndStatus(String id, String status);
}
