package com.example.corespring.core.enums;


import com.example.corespring.config.messages.MessageUtils;
import com.example.corespring.core.constants.Constants;
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
    SUCCESS(Constants.ResponseCode.SUCCESS, MessageUtils.getMessage("success"), HttpStatus.OK),

    //Excel
    HANDLE_READ_EXCEL_ERROR(Constants.ResponseCode.SYSTEM_ERROR, MessageUtils.getMessage("excel.error.processing"), HttpStatus.INTERNAL_SERVER_ERROR),
    FORMAT_HEADER_ERROR(Constants.ResponseCode.BAD_REQUEST, MessageUtils.getMessage("excel.error.header_not_match"), HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_INPUT_DATA_REQUIRED(Constants.ResponseCode.BAD_REQUEST, MessageUtils.getMessage("excel.input.data_required"), HttpStatus.BAD_REQUEST),


    FILE_NOT_FOUND(Constants.ResponseCode.SYSTEM_ERROR, MessageUtils.getMessage("file.not_found"), HttpStatus.NOT_FOUND),
    INVALID_FILE(Constants.ResponseCode.BAD_REQUEST, MessageUtils.getMessage("file.invalid"), HttpStatus.BAD_REQUEST),
    INVALID_FILE_FORMAT(Constants.ResponseCode.BAD_REQUEST, MessageUtils.getMessage("file.format_invalid"), HttpStatus.BAD_REQUEST),

    ;

    String code;
    String message;
    HttpStatusCode httpStatus;
}
