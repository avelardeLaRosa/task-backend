package com.task.app;

import com.task.app.entities.TaskEntity;
import com.task.app.repository.ITaskRepository;
import com.task.app.util.IServiceConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Date;
import java.util.List;

@EnableSwagger2
@EnableScheduling
@SpringBootApplication
public class TaskBackendApplication {

    @Autowired
    public TaskBackendApplication(ITaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(TaskBackendApplication.class, args);
    }

    private final ITaskRepository taskRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void tasksValidity() {
        List<TaskEntity> taskEntities = this.taskRepository.findAllByStatusAndStatusOperationNotLike(
                IServiceConstants.CREATED,
                IServiceConstants.CLOSED
        );
        if (!taskEntities.isEmpty()) {
            taskEntities.forEach(t -> {
                if (t.getFechaFin().before(new Date())) {
                    t.setStatusOperation(IServiceConstants.CLOSED);
                    this.taskRepository.save(t);
                }
            });
        }
    }

}
