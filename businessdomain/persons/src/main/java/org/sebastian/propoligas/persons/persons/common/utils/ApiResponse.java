package org.sebastian.propoligas.persons.persons.common.utils;

import java.time.LocalDateTime;

public class ApiResponse<T> {

    private T data;
    private Meta meta;

    public ApiResponse(T data, Meta meta) {
        this.data = data;
        this.meta = meta;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public static class Meta {

        private String message;
        private Integer code;
        private LocalDateTime date;

        public Meta(String message, Integer code, LocalDateTime date) {
            this.message = message;
            this.code = code;
            this.date = date;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public LocalDateTime getDate() {
            return date;
        }

        public void setDate(LocalDateTime date) {
            this.date = date;
        }



    }

}
