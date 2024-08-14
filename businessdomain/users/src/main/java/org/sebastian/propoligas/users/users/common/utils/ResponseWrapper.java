package org.sebastian.propoligas.users.users.common.utils;

public class ResponseWrapper<T> {

    private T data;
    private String errorMessage;

    public ResponseWrapper(T data, String errorMessage) {
        this.data = data;
        this.errorMessage = errorMessage;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
