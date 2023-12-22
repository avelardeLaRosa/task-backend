package com.task.app.util.interfaces;

import com.task.app.util.ApiResponse;
import com.task.app.util.Response;

import java.util.Map;

public interface IGenericCrud<T> {


    ApiResponse<Response<T>> add(T t);

    ApiResponse<Response<T>> update(T t);

    ApiResponse<Response<T>> get(String id);

    ApiResponse<Response<T>> delete(String id);
}
