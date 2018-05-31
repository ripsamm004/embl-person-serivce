package com.embl.personservice.api.exception;

import com.embl.personservice.api.ErrorEnum;

public class BadRequestException extends BaseHTTPException {

    public BadRequestException(String logMessage, ErrorEnum error){
        super(logMessage, error);
    }

}
