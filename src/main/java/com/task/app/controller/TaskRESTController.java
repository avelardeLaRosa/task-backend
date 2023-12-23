package com.task.app.controller;

import com.task.app.dto.TaskDTO;
import com.task.app.service.ITaskService;
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

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/task")
@CrossOrigin(origins = "http://localhost:4200")
public class TaskRESTController {

    private final ITaskService taskService;

    @Autowired
    public TaskRESTController(ITaskService taskService) {
        this.taskService = taskService;
    }


    @GetMapping
    public ResponseEntity<ApiResponse<Response<TaskDTO>>> getTaks(
            @RequestParam(value = IServiceConstants.PAGE, defaultValue = IServiceConstants.PAGE_NUMBER, required = false) String page,
            @RequestParam(value = IServiceConstants.SIZE, defaultValue = IServiceConstants.PAGE_SIZE, required = false) String size,
            @RequestParam(value = IServiceConstants.F_INICIO, defaultValue = "", required = false) String fechaInicio,
            @RequestParam(value = IServiceConstants.F_FIN, defaultValue = "", required = false) String fechaFin,
            @RequestParam(value = IServiceConstants.STATUS_OP, defaultValue = "", required = false) String statusOp
    ) throws ParseException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(IServiceConstants.PAGE, page);
        parameters.put(IServiceConstants.SIZE, size);
        parameters.put(IServiceConstants.F_INICIO, fechaInicio);
        parameters.put(IServiceConstants.F_FIN, fechaFin);
        parameters.put(IServiceConstants.STATUS_OP, statusOp);

        Pagination<TaskDTO> pagination = this.taskService.getPagination(parameters);
        Response<TaskDTO> response = new Response<>();

        response.setPagination(pagination);
        ApiResponse<Response<TaskDTO>> apiResponse = new ApiResponse<>();
        apiResponse.success(Messages.OK.getCode(), Messages.OK.getMessage(), response);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Response<TaskDTO>>> getTask(@PathVariable("id") String id) {
        try {
            if (StringUtils.isEmpty(id)) {
                throw new GlobalException(String.valueOf(Messages.BADREQUEST.getCode()), IServiceConstants.ID_NULL);
            }
            ApiResponse<Response<TaskDTO>> taskFound = this.taskService.get(id);
            if (!taskFound.getExitoso()) {
                throw new GlobalException(String.valueOf(taskFound.getCode()), taskFound.getMessages());
            }
            return new ResponseEntity<>(new ApiResponse<Response<TaskDTO>>().success(Messages.OK.getCode(), taskFound.getMessages(), taskFound.getData()), HttpStatus.OK);
        } catch (GlobalException g) {
            return new ResponseEntity<>(new ApiResponse<Response<TaskDTO>>().failed(400, g.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Response<TaskDTO>>> add(
            @RequestBody TaskDTO taskDTO
    ) {
        try {
            if (taskDTO == null) {
                throw new GlobalException(String.valueOf(Messages.BADREQUEST.getCode()), IServiceConstants.DTO_NULL);
            }
            ApiResponse<Response<TaskDTO>> taskFound = this.taskService.add(taskDTO);
            if (!taskFound.getExitoso()) {
                throw new GlobalException(String.valueOf(taskFound.getCode()), taskFound.getMessages());
            }
            return new ResponseEntity<>(new ApiResponse<Response<TaskDTO>>().success(Messages.OK.getCode(), taskFound.getMessages(), taskFound.getData()), HttpStatus.OK);
        } catch (GlobalException g) {
            return new ResponseEntity<>(new ApiResponse<Response<TaskDTO>>().failed(400, g.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity<ApiResponse<Response<TaskDTO>>> update(
            @RequestBody TaskDTO taskDTO
    ) {
        try {
            if (taskDTO == null) {
                throw new GlobalException(String.valueOf(Messages.BADREQUEST.getCode()), IServiceConstants.DTO_NULL);
            }
            ApiResponse<Response<TaskDTO>> taskFound = this.taskService.update(taskDTO);
            if (!taskFound.getExitoso()) {
                throw new GlobalException(String.valueOf(taskFound.getCode()), taskFound.getMessages());
            }
            return new ResponseEntity<>(new ApiResponse<Response<TaskDTO>>().success(Messages.OK.getCode(), taskFound.getMessages(), taskFound.getData()), HttpStatus.OK);
        } catch (GlobalException g) {
            return new ResponseEntity<>(new ApiResponse<Response<TaskDTO>>().failed(400, g.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Response<TaskDTO>>> delete(
            @PathVariable("id") String id
    ) {
        try {
            if (StringUtils.isEmpty(id)) {
                throw new GlobalException(String.valueOf(Messages.BADREQUEST.getCode()), IServiceConstants.ID_NULL);
            }
            ApiResponse<Response<TaskDTO>> taskFound = this.taskService.delete(id);
            if (!taskFound.getExitoso()) {
                throw new GlobalException(String.valueOf(taskFound.getCode()), taskFound.getMessages());
            }
            return new ResponseEntity<>(new ApiResponse<Response<TaskDTO>>().success(Messages.OK.getCode(), Messages.OK.getMessage(), taskFound.getData()), HttpStatus.OK);
        } catch (GlobalException g) {
            return new ResponseEntity<>(new ApiResponse<Response<TaskDTO>>().failed(Integer.parseInt(g.getCode()), g.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

}
