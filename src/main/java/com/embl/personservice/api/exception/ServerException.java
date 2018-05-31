package com.embl.personservice.api.exception;

import com.embl.personservice.api.ErrorEnum;

public class ServerException extends BaseHTTPException {

    public ServerException(String logMessage, ErrorEnum error){
        super(logMessage, error);
    }
}
