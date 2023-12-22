package com.task.app.service;

import com.task.app.dto.TaskDTO;
import com.task.app.dto.UserDTO;
import com.task.app.util.Pagination;
import com.task.app.util.interfaces.IGenericCrud;

import java.text.ParseException;
import java.util.Map;

public interface ITaskService extends IGenericCrud<TaskDTO> {
    Pagination<TaskDTO> getPagination(Map<String, Object> parameters) throws ParseException;

}
