package com.cloud.secure.streaming.common.exceptions;

import com.cloud.secure.streaming.common.utilities.ResponseUtil;
import com.cloud.secure.streaming.common.utilities.RestAPIResponse;
import com.cloud.secure.streaming.common.utilities.RestAPIStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

/**
 * @author 689Cloud
 */
@ControllerAdvice
public class ValidationExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private ResponseUtil responseUtil;

    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(value = ApplicationException.class)
    public ResponseEntity<RestAPIResponse> handleApplicationException(ApplicationException ex, WebRequest request) {

        LOGGER.debug("handleApplicationException", ex);

        ResponseEntity<RestAPIResponse> response;
        if (ex.getApiStatus() == RestAPIStatus.BAD_REQUEST) {
            // handle bad request
            response = responseUtil.buildResponse(RestAPIStatus.BAD_REQUEST, ex.getMessage(), HttpStatus.MULTI_STATUS);
        } else {
            response = responseUtil.buildResponse(ex.getApiStatus(), ex.getMessage(), HttpStatus.OK);
        }

        return response;
    }

    // when missing parameter
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers, HttpStatus status, WebRequest request) {

        return new ResponseEntity<Object>(new RestAPIResponse(RestAPIStatus.BAD_REQUEST, ex.getMessage()), headers, status);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<RestAPIResponse> handleUncatchException(Exception ex, WebRequest request) {

        LOGGER.error("handleUncatchException", ex);
        return responseUtil.buildResponse(RestAPIStatus.INTERNAL_SERVER_ERROR, "Please contact System SysAdmin to resolve problem", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        final String FIELD_NAME_PATTERN = "\\{fieldName}";
        StringBuilder sb = new StringBuilder();
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        for(FieldError fieldError: fieldErrors){
            String message = "";
            if(fieldError.getDefaultMessage() != null){
                message = fieldError.getDefaultMessage();
                message = message.replaceFirst(FIELD_NAME_PATTERN, fieldError.getField());
            }

            sb.append(message).append("; ");
        }

        return new ResponseEntity<Object>(new RestAPIResponse(RestAPIStatus.BAD_REQUEST, sb.toString()), headers, status);
    }

}
