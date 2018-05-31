package com.embl.personservice.api;


public class ApiError {

    private String code;

    private String message;

    public ApiError(String code, String message){
        this.code       = code;
        this.message    = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ApiError apiError = (ApiError) o;

        if (code != null ? !code.equals(apiError.code) : apiError.code != null) return false;
        return !(message != null ? !message.equals(apiError.message) : apiError.message != null);

    }

    @Override
    public String toString() {
        return "ApiError{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}



