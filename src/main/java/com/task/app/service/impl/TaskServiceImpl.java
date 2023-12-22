package com.task.app.service.impl;

import com.task.app.dto.TaskDTO;
import com.task.app.entities.TaskEntity;
import com.task.app.entities.UserEntity;
import com.task.app.repository.ITaskRepository;
import com.task.app.repository.IUserRepository;
import com.task.app.service.ITaskService;
import com.task.app.util.ApiResponse;
import com.task.app.util.Date;
import com.task.app.util.Pagination;
import com.task.app.util.Response;
import com.task.app.util.interfaces.IServiceConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class TaskServiceImpl implements ITaskService {

    private final ITaskRepository taskRepository;
    private final IUserRepository userRepository;

    @Autowired
    public TaskServiceImpl(ITaskRepository taskRepository, IUserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Transactional(rollbackFor = {RuntimeException.class})
    @Override
    public ApiResponse<Response<TaskDTO>> add(TaskDTO taskDTO) {
        TaskEntity task = new TaskEntity();
        task.setEntity(taskDTO);
        task.setTaskCode(IServiceConstants.generateCodeMaintenance(IServiceConstants.TASK_PREFIX, taskRepository.count() + 1, IServiceConstants.ENTITY_LENGTH_CODE));
        task.setCreateDate(Date.getCurrent(IServiceConstants.TIME_ZONE_DEFAULT));
        task.setStatus(IServiceConstants.CREATED);
        task.setStatusOperation(IServiceConstants.ACTIVE);
        task.setUniqueIdentifier(UUID.randomUUID().toString());

        Optional<UserEntity> optionalUser = this.userRepository.findByUniqueIdentifierAndStatus(taskDTO.getUserDTO().getId(), IServiceConstants.CREATED);
        if (optionalUser.isEmpty()) {
            return getErrorApiResponse(400, IServiceConstants.USER_NOT_FOUND_MESSAGE);
        }
        task.setUserEntity(optionalUser.get());
        return saveEntityAndApiResponse(200, String.format(IServiceConstants.TASK_SAVED_MESSAGE, task.getTaskCode()), task);
    }

    @Transactional(rollbackFor = {RuntimeException.class})
    @Override
    public ApiResponse<Response<TaskDTO>> update(TaskDTO taskDTO) {

        Optional<UserEntity> userFound = this.userRepository.findByUniqueIdentifierAndStatus(taskDTO.getUserDTO().getId(), IServiceConstants.CREATED);
        if (userFound.isEmpty()) {
            return getErrorApiResponse(400, IServiceConstants.USER_NOT_FOUND_MESSAGE);
        }
        Optional<TaskEntity> optionalTask = this.taskRepository.findByUniqueIdentifierAndStatusOperationNotLike(taskDTO.getId(), IServiceConstants.CLOSED);
        if (optionalTask.isEmpty()) {
            return getErrorApiResponse(400, IServiceConstants.TASK_NOT_FOUND_MESSAGE);
        }
        TaskEntity taskEntity = optionalTask.get();
        UserEntity userEntity = userFound.get();
        if (!taskEntity.getStatusOperation().equals(IServiceConstants.CLOSED)) {
            taskEntity.setStatusOperation(StringUtils.isEmpty(taskDTO.getStatusOperation()) ? null : taskEntity.getStatusOperation());
        }
        taskEntity.setUpdateDate(Date.getCurrent(IServiceConstants.TIME_ZONE_DEFAULT));
        taskEntity.setDescripcion(StringUtils.isEmpty(taskDTO.getDescripcion()) ? null : taskEntity.getDescripcion());
        taskEntity.setFechaInicio(taskDTO.getFechaInicio() == null ? taskEntity.getFechaInicio() : taskDTO.getFechaInicio());
        taskEntity.setFechaInicio(taskDTO.getFechaFin() == null ? taskEntity.getFechaFin() : taskDTO.getFechaFin());
        taskEntity.setUserEntity(userEntity);
        return saveEntityAndApiResponse(200, String.format(IServiceConstants.TASK_UPDATED_MESSAGE, taskEntity.getTaskCode()), taskEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse<Response<TaskDTO>> get(String id) {
        Optional<TaskEntity> optionalTask = this.taskRepository.findByUniqueIdentifierAndStatus(id, IServiceConstants.CREATED);
        if (optionalTask.isEmpty()) {
            return getErrorApiResponse(400, IServiceConstants.TASK_NOT_FOUND_MESSAGE);
        }
        return this.getSucessApiResponse(200, String.format(IServiceConstants.TASK_FOUND_MESSAGE, optionalTask.get().getTaskCode()), optionalTask.get().getDTO());
    }

    @Transactional(rollbackFor = {RuntimeException.class})
    @Override
    public ApiResponse<Response<TaskDTO>> delete(String id) {
        Optional<TaskEntity> optionalTask = this.taskRepository.findByUniqueIdentifierAndStatus(id, IServiceConstants.CREATED);
        if (optionalTask.isEmpty()) {
            return getErrorApiResponse(400, IServiceConstants.TASK_NOT_FOUND_MESSAGE);
        }
        TaskEntity taskEntity = optionalTask.get();
        taskEntity.setStatus(IServiceConstants.DELETED);
        taskEntity.setDeleteDate(Date.getCurrent(IServiceConstants.TIME_ZONE_DEFAULT));
        return saveEntityAndApiResponse(200, String.format(IServiceConstants.TASK_DELETED_MESSAGE, taskEntity.getTaskCode()), taskEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public Pagination<TaskDTO> getPagination(Map<String, Object> parameters) {
        return null;
    }

    public ApiResponse<Response<TaskDTO>> getSucessApiResponse(Integer code, String message, TaskDTO data) {
        Response<TaskDTO> response = new Response<>();
        response.setT(data);
        return new ApiResponse<Response<TaskDTO>>().success(code, message, response);
    }

    public ApiResponse<Response<TaskDTO>> buildErrorMessage(Integer code, String message) {
        return new ApiResponse<Response<TaskDTO>>().failed(code, message);
    }

    public ApiResponse<Response<TaskDTO>> getErrorApiResponse(Integer code, String message) {
        return new ApiResponse<Response<TaskDTO>>().failed(code, message);
    }

    public ApiResponse<Response<TaskDTO>> saveEntityAndApiResponse(Integer code, String message, TaskEntity entity) {
        Response<TaskDTO> response = new Response<>();
        response.setT(this.taskRepository.save(entity).getDTO());
        return new ApiResponse<Response<TaskDTO>>().success(code, message, response);
    }
}
