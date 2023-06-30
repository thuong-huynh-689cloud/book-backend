package com.cloud.secure.streaming.common.utilities;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * 
 * @author 689Cloud
 * @param <T> 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestAPIResponse<T extends Object> implements Serializable {

    /**
     * status & message fields have not setter. They are assigned value when
     * initial by APIStatus parameter
     */
    private int status;
    private String message;
    private T data;
    private String description;

    public RestAPIResponse(RestAPIStatus restApiStatus, T data) {

        if (restApiStatus == null) {
            throw new IllegalArgumentException("API Status must not be null");
        }

        this.status = restApiStatus.getCode();
        this.message = restApiStatus.getDescription();
        this.data = data;
        this.description = "";
    }

    public RestAPIResponse(RestAPIStatus restApiStatus, T data, String description) {

        if (restApiStatus == null) {
            throw new IllegalArgumentException("API Status must not be null");
        }

        this.status = restApiStatus.getCode();
        this.message = restApiStatus.getDescription();
        this.data = data;
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
