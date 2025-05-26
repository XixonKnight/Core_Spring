package com.example.corespring.enums;


import com.example.corespring.config.messages.MessageUtils;
import com.example.corespring.constants.Constants;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public enum ErrorCodes {
    SYSTEM_ERROR(Constants.ResponseCode.SYSTEM_ERROR, MessageUtils.getMessage("error.system"), HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHORIZED(Constants.ResponseCode.UNAUTHORIZED, MessageUtils.getMessage("error.unauthorized"), HttpStatus.UNAUTHORIZED),
    FORBIDDEN(Constants.ResponseCode.FORBIDDEN, MessageUtils.getMessage("error.forbidden"), HttpStatus.FORBIDDEN),
    RESOURCE_NOT_FOUND(Constants.ResponseCode.NOT_FOUND, MessageUtils.getMessage("error.not_found"), HttpStatus.NOT_FOUND),
    SUCCESS(Constants.ResponseCode.SUCCESS, MessageUtils.getMessage("success"), HttpStatus.OK)
    ;

    String code;
    String message;
    HttpStatusCode httpStatus;
}
