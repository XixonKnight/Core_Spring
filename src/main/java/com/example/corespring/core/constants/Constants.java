package com.example.corespring.core.constants;

import lombok.experimental.UtilityClass;

/**
 * Created by NhanNguyen on 1/18/2021
 *
 * @author : NhanNguyen
 * @since : 1/18/2021
 */
@UtilityClass
public class Constants {

    /**
     * Created by NhanNguyen on 1/18/2021
     * RESPONSE_TYPE
     *
     * @author : NhanNguyen
     * @since : 1/18/2021
     */
    @UtilityClass
    public class ResponseType {
        public final String SUCCESS = "SUCCESS";
        public final String ERROR = "ERROR";
        public final String WARNING = "WARNING";
        public final String CONFIRM = "CONFIRM";
        public final String invalidPermission = "invalidPermission";

        public final String doesNotExist = "doesNotExist";
        public final String OK = "OK";
        public final String NOK = "NOK";
        public final String EXIST = "EXIST";

    }

    /**
     * Created by NhanNguyen on 1/18/2021
     * ResponseCode
     *
     * @author : NhanNguyen
     * @since : 1/18/2021
     */

    @UtilityClass
    public class ResponseCode {
        public final String SUCCESS = "200";
        public final String BAD_REQUEST = "400";
        public final String SYSTEM_ERROR = "500";
        public final String UNAUTHORIZED = "401";
        public final String FORBIDDEN = "403";
        public final String NOT_FOUND = "404";
        public final String DELETE_SUCCESS = "deleteSuccess";
        public final String UPDATE_STATUS_SUCCESS = "updateStatusSuccess";
        public final String UPDATE_SUCCESS = "updateSuccess";
        public final String ERROR = "error";
        public final String WARNING = "warning";
        public final String RECORD_DELETED = "record.deleted";
        public final String EMAIL_ADDRESS_DELETED = "emailAddress.deleted";
        public final String RECORD_IN_USED = "record.inUsed";
        public final String DOCUMENT_TYPE_EXISTED = "documentTypeExits";
        public final String DUPLICATE_DATA_REDUCTION = "taxReduction.duplicateData";
        public final String PARAMETER_USED = "parameterUsed";
        public final String NOT_ALLOWED_DELETE_DATA_TYPE = "dataType.recordInUsed";
        public final String NOT_ALLOWED_DELETE_FORMULA = "formula.config.cannotDelete";
        public final String NOT_ALLOWED_CHANGE_STATUS_FORMULA = "formula.config.cannotChange";
        public final String NOT_ALLOWED_EVALUATION = "evaluation.cannotEvaluation";
        public final String NO_RECORDS = "evaluation.noRecords";
        public final String LOCK_UNIT = "evaluation.orgLocked";
        public final String NO_DATA_EVALUATION = "evaluation.noData";
        public final String PAYROLL_IS_LOCK = "ERROR.payroll.calculate.isLock";
        public final String PAYROLL_IS_PAID = "ERROR.payroll.calculate.paid";
        public final String ROLE_EXIST = "permission.role.exist";
        public final String MENU_HAVE_CHILD = "permission.menu.haveChild";
        public final String ERROR_COMPOSITE = "error.composite";
        public final String SUCCESS_COMPOSITE = "success.composite";
        public final String ERROR_SEND = "error.send";
        public final String SUCCESS_SEND = "success.send";
        public final String SUCCESS_SAVE = "success.save";
        public final String DELETE_ERROR = "error.delete";
        public final String SAVE_DUPLICATE_CODE = "save.duplicateCode";
        public final String DOMAIN_DUPLICATE_CODE = "permission.duplicateDomain";
        public final String SYNC_TAX_SUCCESS = "syncTax.success";
        public final String SYNC_TAX_ERROR = "syncTax.error";
    }

    @UtilityClass
    public class FormatDate {
        public final String PATTERN_YYYY_MM_DD = "yyyy-MM-dd";
        public final String PATTERN_DD_MM = "dd/MM";
        public final String PATTERN_DD_MM_YYYY = "dd/MM/yyyy";
    }

    @UtilityClass
    public class Key {
        public final String DASH = "-";
    }
}
