package com.embl.personservice.api;

public enum ErrorEnum {


    // SERVER ERRORS
    INTERNAL_ERROR                              ("M0001","WE ARE EXPERIENCING SOME ISSUES, PLEASE TRY LATER"),
    NO_FOUND_EXCEPTION                          ("M0002","NO FOUND EXCEPTION"),
    BAD_REQUEST_EXCEPTION                       ("M0003","BAD REQUEST EXCEPTION"),
    FORBIDDEN_EXCEPTION                         ("M0004","FORBIDDEN EXCEPTION"),

    // API CONFIG ERRORS
    API_ERROR_PERSON_ALREADY_EXIST              ("A0001","PERSON ALREADY EXIST"),
    API_ERROR_PERSON_NOT_FOUND                  ("A0001","PERSON NOT FOUND"),
    API_ERROR_PERSON_NAME_NOT_CORRECT           ("A0003","PERSON NAME NOT CORRECT"),
    API_ERROR_PERSON_NAME_NOT_MATCH             ("A0004","PERSON NAME NOT MATCH WITH REQUEST BODY"),
    API_ERROR_PERSON_AGE_CORRECT                ("A0005","AGE FORMAT NOT CORRECT"),
    API_ERROR_PERSON_REQUEST_PARAM_NOT_MATCH    ("A0006","UPDATE REQUEST PARAM NOT MATCH")
    ;

    private String code;
    private String message;

    ErrorEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return code+","+message;
    }

    public static ErrorEnum getByCode(String code) {
        if (code == null) return null;
        for(ErrorEnum ec : values()) {
            if( (ec.code).equalsIgnoreCase(code)){
                return ec;
            }
        }
        return INTERNAL_ERROR;
    }
}
