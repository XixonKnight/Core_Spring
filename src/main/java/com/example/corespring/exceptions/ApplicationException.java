package com.example.corespring.exceptions;


import com.example.corespring.enums.ErrorCodes;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApplicationException extends RuntimeException {
    ErrorCodes errorCode;

    public ApplicationException(ErrorCodes errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
