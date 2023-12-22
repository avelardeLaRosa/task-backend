package com.task.app.service.impl;

import com.task.app.dto.UserDTO;
import com.task.app.entities.UserEntity;
import com.task.app.repository.IUserRepository;
import com.task.app.service.IUserService;
import com.task.app.util.ApiResponse;
import com.task.app.util.Date;
import com.task.app.util.Pagination;
import com.task.app.util.Response;
import com.task.app.util.interfaces.IServiceConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;

    @Autowired
    public UserServiceImpl(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Transactional(rollbackFor = {RuntimeException.class})
    @Override
    public ApiResponse<Response<UserDTO>> add(UserDTO userDTO) {
        Optional<UserEntity> optionalUser = this.userRepository.findByDniAndStatus(userDTO.getDni(), IServiceConstants.CREATED);
        if (optionalUser.isPresent()) {
            return buildErrorMessage(400, String.format(IServiceConstants.USER_FOUND_MESSAGE, userDTO.getDni()));
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setEntity(userDTO);
        userEntity.setCreateDate(Date.getCurrent(IServiceConstants.TIME_ZONE_DEFAULT));
        userEntity.setStatus(IServiceConstants.CREATED);
        userEntity.setUniqueIdentifier(UUID.randomUUID().toString());
        return saveEntityAndApiResponse(201, String.format(IServiceConstants.USER_SAVED_MESSAGE, userDTO.getDni()), userEntity);
    }

    @Transactional(rollbackFor = {RuntimeException.class})
    @Override
    public ApiResponse<Response<UserDTO>> update(UserDTO userDTO) {
        Optional<UserEntity> optionalUser = this.userRepository.findByUniqueIdentifierAndStatus(userDTO.getId(), IServiceConstants.CREATED);
        if (optionalUser.isEmpty()) {
            return getErrorApiResponse(400, IServiceConstants.USER_NOT_FOUND_MESSAGE);
        }
        UserEntity userEntity = optionalUser.get();
        userEntity.setUpdateDate(Date.getCurrent(IServiceConstants.TIME_ZONE_DEFAULT));
        userEntity.setNombre(StringUtils.isEmpty(userDTO.getNombre()) ? optionalUser.get().getNombre() : userDTO.getNombre());
        userEntity.setApellido(StringUtils.isEmpty(userDTO.getApellido()) ? optionalUser.get().getApellido() : userDTO.getApellido());
        userEntity.setCargo(StringUtils.isEmpty(userDTO.getCargo()) ? optionalUser.get().getCargo() : userDTO.getCargo());
        userEntity.setDni(StringUtils.isEmpty(userDTO.getDni()) ? optionalUser.get().getDni() : userDTO.getDni());
        userEntity.setTelefono(StringUtils.isEmpty(userDTO.getTelefono()) ? optionalUser.get().getTelefono() : userDTO.getTelefono());
        return saveEntityAndApiResponse(200, String.format(IServiceConstants.USER_UPDATED_MESSAGE, userEntity.getDni()), userEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse<Response<UserDTO>> get(String id) {
        Optional<UserEntity> optionalUser = this.userRepository.findByUniqueIdentifierAndStatus(id, IServiceConstants.CREATED);
        if (optionalUser.isEmpty()) {
            return getErrorApiResponse(400, IServiceConstants.USER_NOT_FOUND_MESSAGE);
        }
        return this.getSucessApiResponse(200, String.format(IServiceConstants.USER_FOUND_MESSAGE, optionalUser.get().getDni()), optionalUser.get().getDTO());
    }

    @Transactional(rollbackFor = {RuntimeException.class})
    @Override
    public ApiResponse<Response<UserDTO>> delete(String id) {
        Optional<UserEntity> optionalUser = this.userRepository.findByUniqueIdentifierAndStatus(id, IServiceConstants.CREATED);
        if (optionalUser.isEmpty()) {
            return getErrorApiResponse(400, IServiceConstants.USER_NOT_FOUND_MESSAGE);
        }
        UserEntity userEntity = optionalUser.get();
        userEntity.setStatus(IServiceConstants.DELETED);
        userEntity.setDeleteDate(Date.getCurrent(IServiceConstants.TIME_ZONE_DEFAULT));
        return saveEntityAndApiResponse(200, String.format(IServiceConstants.USER_DELETED_MESSAGE, userEntity.getDni()), userEntity);
    }

    public ApiResponse<Response<UserDTO>> getSucessApiResponse(Integer code, String message, UserDTO data) {
        Response<UserDTO> response = new Response<>();
        response.setT(data);
        return new ApiResponse<Response<UserDTO>>().success(code, message, response);
    }

    public ApiResponse<Response<UserDTO>> buildErrorMessage(Integer code, String message) {
        return new ApiResponse<Response<UserDTO>>().failed(code, message);
    }

    public ApiResponse<Response<UserDTO>> getErrorApiResponse(Integer code, String message) {
        return new ApiResponse<Response<UserDTO>>().failed(code, message);
    }

    public ApiResponse<Response<UserDTO>> saveEntityAndApiResponse(Integer code, String message, UserEntity entity) {
        Response<UserDTO> response = new Response<>();
        response.setT(this.userRepository.save(entity).getDTO());
        return new ApiResponse<Response<UserDTO>>().success(code, message, response);
    }

    @Transactional(readOnly = true)
    @Override
    public Pagination<UserDTO> getPagination(Map<String, Object> parameters) {
        Pagination<UserDTO> pagination = new Pagination<>();
        Pageable pageable = PageRequest.of(
                Integer.parseInt(parameters.get(IServiceConstants.PAGE).toString()),
                Integer.parseInt(parameters.get(IServiceConstants.SIZE).toString())
        );
        Page<UserEntity> userEntities = this.userRepository.getUserEntities(
                IServiceConstants.CREATED,
                parameters.get(IServiceConstants.FILTER).toString(),
                StringUtils.isEmpty(parameters.get(IServiceConstants.FILTER).toString()) ? null : parameters.get(IServiceConstants.FILTER).toString(),
                pageable
        );

        pagination.setList(userEntities.getContent().stream().map(UserEntity::getDTO).collect(Collectors.toList()));
        pagination.setTotalPages(userEntities.getTotalPages());
        pagination.setPageNumber(userEntities.getNumber());
        pagination.setPageSize(userEntities.getSize());
        pagination.setTotalElements(userEntities.getTotalElements());
        pagination.setLastRow(userEntities.isLast());
        return pagination;
    }
}
