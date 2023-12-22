package com.task.app.service;

import com.task.app.dto.UserDTO;
import com.task.app.util.Pagination;
import com.task.app.util.interfaces.IGenericCrud;

import java.util.Map;

public interface IUserService extends IGenericCrud<UserDTO> {
    Pagination<UserDTO> getPagination(Map<String, Object> parameters);
}
