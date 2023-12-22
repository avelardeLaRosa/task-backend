package com.task.app.util.interfaces;

public interface IDTOManagment<T> {
    T getDTO();

    void setEntity(T t);
}
