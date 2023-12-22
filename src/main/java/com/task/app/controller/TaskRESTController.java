package com.task.app.controller;

import com.task.app.service.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/task")
@CrossOrigin(origins = "http://localhost:4200")
public class TaskRESTController {

    private final ITaskService taskService;

    @Autowired
    public TaskRESTController(ITaskService taskService) {
        this.taskService = taskService;
    }
}
