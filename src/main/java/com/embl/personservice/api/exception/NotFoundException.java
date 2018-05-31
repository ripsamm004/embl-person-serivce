package com.embl.personservice.api.exception;

import com.embl.personservice.api.ErrorEnum;

public class NotFoundException extends BaseHTTPException {
    public NotFoundException(String logMessage, ErrorEnum error){
        super(logMessage, error);
    }
}
