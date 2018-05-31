package com.embl.personservice.exception;

import com.embl.personservice.api.ErrorEnum;

public class ValidatorPersonAlreadyExistException extends ValidatorGenericException{


    public ValidatorPersonAlreadyExistException(String fieldName, ErrorEnum errorCode){
        super(fieldName, errorCode);
    }

    @Override
    public String getMessage(){
        return this.fieldName + " person already exist";
    }
}
