package com.task.app.controller;

import com.task.app.dto.UserDTO;
import com.task.app.service.IUserService;
import com.task.app.util.ApiResponse;
import com.task.app.util.Messages;
import com.task.app.util.Pagination;
import com.task.app.util.Response;
import com.task.app.util.exception.GlobalException;
import com.task.app.util.IServiceConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:4200")
public class UserRESTController {
    private final IUserService userService;

    @Autowired
    public UserRESTController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Response<UserDTO>>> getUsers(
            @RequestParam(value = IServiceConstants.FILTER, defaultValue = "", required = false) String filter,
            @RequestParam(value = IServiceConstants.CARGO, defaultValue = "", required = false) String cargo,
            @RequestParam(value = IServiceConstants.PAGE, defaultValue = IServiceConstants.PAGE_NUMBER, required = false) String page,
            @RequestParam(value = IServiceConstants.SIZE, defaultValue = IServiceConstants.PAGE_SIZE, required = false) String size
    ) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(IServiceConstants.FILTER, filter);
        parameters.put(IServiceConstants.CARGO, cargo);
        parameters.put(IServiceConstants.PAGE, page);
        parameters.put(IServiceConstants.SIZE, size);

        Pagination<UserDTO> pagination = this.userService.getPagination(parameters);
        Response<UserDTO> response = new Response<>();

        response.setPagination(pagination);
        ApiResponse<Response<UserDTO>> apiResponse = new ApiResponse<>();
        apiResponse.success(Messages.OK.getCode(), Messages.OK.getMessage(), response);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Response<UserDTO>>> getUser(@PathVariable("id") String id) {
        try {
            if (StringUtils.isEmpty(id)) {
                throw new GlobalException(String.valueOf(Messages.BADREQUEST.getCode()), IServiceConstants.ID_NULL);
            }
            ApiResponse<Response<UserDTO>> userFound = this.userService.get(id);
            if (!userFound.getExitoso()) {
                throw new GlobalException(String.valueOf(userFound.getCode()), userFound.getMessages());
            }
            return new ResponseEntity<>(new ApiResponse<Response<UserDTO>>().success(Messages.OK.getCode(), Messages.OK.getMessage(), userFound.getData()), HttpStatus.OK);
        } catch (GlobalException g) {
            return new ResponseEntity<>(new ApiResponse<Response<UserDTO>>().failed(400, g.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Response<UserDTO>>> add(
            @RequestBody UserDTO userDTO
    ) {
        try {
            if (userDTO == null) {
                throw new GlobalException(String.valueOf(Messages.BADREQUEST.getCode()), IServiceConstants.DTO_NULL);
            }
            ApiResponse<Response<UserDTO>> userFound = this.userService.add(userDTO);
            if (!userFound.getExitoso()) {
                throw new GlobalException(String.valueOf(userFound.getCode()), userFound.getMessages());
            }
            return new ResponseEntity<>(new ApiResponse<Response<UserDTO>>().success(Messages.CREATED.getCode(), userFound.getMessages(), userFound.getData()), HttpStatus.OK);
        } catch (GlobalException g) {
            return new ResponseEntity<>(new ApiResponse<Response<UserDTO>>().failed(400, g.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity<ApiResponse<Response<UserDTO>>> update(
            @RequestBody UserDTO userDTO
    ) {
        try {
            if (userDTO == null) {
                throw new GlobalException(String.valueOf(Messages.BADREQUEST.getCode()), IServiceConstants.DTO_NULL);
            }
            ApiResponse<Response<UserDTO>> userFound = this.userService.update(userDTO);
            if (!userFound.getExitoso()) {
                throw new GlobalException(String.valueOf(userFound.getCode()), userFound.getMessages());
            }
            return new ResponseEntity<>(new ApiResponse<Response<UserDTO>>().success(Messages.OK.getCode(), userFound.getMessages(), userFound.getData()), HttpStatus.OK);
        } catch (GlobalException g) {
            return new ResponseEntity<>(new ApiResponse<Response<UserDTO>>().failed(400, g.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Response<UserDTO>>> delete(
            @PathVariable("id") String id
    ) {
        try {
            if (StringUtils.isEmpty(id)) {
                throw new GlobalException(String.valueOf(Messages.BADREQUEST.getCode()), IServiceConstants.ID_NULL);
            }
            ApiResponse<Response<UserDTO>> userFound = this.userService.delete(id);
            if (!userFound.getExitoso()) {
                throw new GlobalException(String.valueOf(userFound.getCode()), userFound.getMessages());
            }
            return new ResponseEntity<>(new ApiResponse<Response<UserDTO>>().success(Messages.OK.getCode(), userFound.getMessages(), userFound.getData()), HttpStatus.OK);
        } catch (GlobalException g) {
            return new ResponseEntity<>(new ApiResponse<Response<UserDTO>>().failed(400, g.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

}
