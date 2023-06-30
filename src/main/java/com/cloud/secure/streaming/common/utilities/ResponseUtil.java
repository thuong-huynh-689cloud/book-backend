package com.cloud.secure.streaming.common.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @author 689Cloud
 */
@Component
public class ResponseUtil {
    private ObjectMapper mapper;

    @Autowired
    public ResponseUtil(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Create HTTP Response
     * @param restApiStatus
     * @param data
     * @return
     */
    private RestAPIResponse _createResponse(RestAPIStatus restApiStatus, Object data) {
        return new RestAPIResponse(restApiStatus, data);
    }

    /**
     * Create HTTP Response with description
     * @param restApiStatus
     * @param data
     * @return
     */
    private RestAPIResponse _createResponse(RestAPIStatus restApiStatus, Object data, String description) {
        return new RestAPIResponse(restApiStatus, data, description);
    }

    /**
     * Build HTTP Response
     * @param restApiStatus
     * @param data
     * @param httpStatus
     * @return
     */
    public ResponseEntity<RestAPIResponse> buildResponse(RestAPIStatus restApiStatus, Object data, HttpStatus httpStatus) {
        return new ResponseEntity(_createResponse(restApiStatus, data), httpStatus);
    }

    /**
     * Build HTTP Response with description
     * @param restApiStatus
     * @param data
     * @param description
     * @param httpStatus
     * @return
     */
    public ResponseEntity<RestAPIResponse> buildResponse(RestAPIStatus restApiStatus, Object data, String description, HttpStatus httpStatus) {
        return new ResponseEntity(_createResponse(restApiStatus, data, description), httpStatus);
    }

    /**
     * Return success HTTP Request
     * @param data
     * @return
     */
    public ResponseEntity<RestAPIResponse> successResponse(Object data) {
        return buildResponse(RestAPIStatus.OK, data, HttpStatus.OK);
    }

    /**
     * Return success HTTP Request with description
     * @param data
     * @param description
     * @return
     */
    public ResponseEntity<RestAPIResponse> successResponse(Object data, String description) {
        return buildResponse(RestAPIStatus.OK, data, description, HttpStatus.OK);
    }
}
