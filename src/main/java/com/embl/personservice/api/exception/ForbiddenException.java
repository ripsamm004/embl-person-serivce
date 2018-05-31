package com.embl.personservice.api.exception;

import com.embl.personservice.api.ErrorEnum;

public class ForbiddenException extends BaseHTTPException {

    public ForbiddenException(String logMessage, ErrorEnum error){
        super(logMessage, error);
    }

}
