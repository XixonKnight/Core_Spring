package com.example.corespring;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by NhanNguyen on 1/18/2021
 *
 * @author: NhanNguyen
 * @date: 1/18/2021
 */
public class Constants {

    /**
     * Created by NhanNguyen on 1/18/2021
     * RESPONSE_TYPE
     *
     * @author: NhanNguyen
     * @date: 1/18/2021
     */
    private static HttpServletRequest req;
    public static String SCHEMA_PAYROLL = "vhr";

    public static class RESPONSE_TYPE {
        public static final String SUCCESS = "SUCCESS";
        public static final String ERROR = "ERROR";
        public static final String WARNING = "WARNING";
        public static final String CONFIRM = "CONFIRM";
        public static final String invalidPermission = "invalidPermission";

        public static final String doesNotExist = "doesNotExist";
        public static final String OK = "OK";
        public static final String NOK = "NOK";
        public static final String EXIST = "EXIST";

    }

    /**
     * Created by NhanNguyen on 1/18/2021
     * RESPONSE_CODE
     *
     * @author: NhanNguyen
     * @date: 1/18/2021
     */

    public static class RESPONSE_CODE {
        public static final String SUCCESS = "success";
        public static final String DELETE_SUCCESS = "deleteSuccess";
        public static final String UPDATE_STATUS_SUCCESS = "updateStatusSuccess";
        public static final String UPDATE_SUCCESS = "updateSuccess";
        public static final String ERROR = "error";
        public static final String WARNING = "warning";
        public static final String RECORD_DELETED = "record.deleted";
        public static final String EMAIL_ADDRESS_DELETED = "emailAddress.deleted";
        public static final String RECORD_INUSED = "record.inUsed";
        public static final String RECORD_NOT_EXISTED = "recordNotExits";
        public static final String POSITION_EXISTED = "positionExits";
        public static final String POSITION_WAGE_EXISTED = "positionWageExits";
        public static final String DOCUMENT_TYPE_EXISTED = "documentTypeExits";
        public static final String NOT_ALLOWED_ADD_EMPLOYEE = "employee.notAllowedAddEmployee";
        public static final String NOT_ALLOWED_DELETE_EMPLOYEE = "employee.notAllowedDeleteEmployee";
        public static final String DUPICATE_DATA_REDUCTION = "taxReduction.duplicateData";
        public static final String PARAMETER_USED = "parameterUsed";
        public static final String SYS_CAT_TYPE_USED = "sysCatTypeUsed";
        public static final String ORG_DUPLICATE_CODE = "organization.duplicateCode";
        public static final String ORG_DUPLICATE_NAME = "organization.duplicateName";
        public static final String NATION_CONFIG_TYPE_USED = "nationConfigTypeUsed";
        public static final String EMP_WORK_SCHEDULE_SUCCESS = "empWorkSchedule.success";
        public static final String NOT_ALLOWED_DELETE_DATA_TYPE = "dataType.recordInUsed";
        public static final String NOT_ALLOWED_DELETE_FORMULA = "formula.config.cannotDelete";
        public static final String NOT_ALLOWED_CHANGE_STATUS_FORMULA = "formula.config.cannotChange";
        public static final String NOT_ALLOWED_EVALUATION = "evaluation.cannotEvaluation";
        public static final String NO_RECORDS = "evaluation.noRecords";
        public static final String LOCK_UNIT = "evaluation.orglocked";
        public static final String NO_DATA_EVALUATION = "evaluation.noData";
        public static final String PAYROLL_IS_LOCK = "ERROR.payroll.calculate.isLock";
        public static final String PAYROLL_IS_PAID = "ERROR.payroll.calculate.paid";
        public static final String ROLE_EXIST = "permission.role.exist";
        public static final String MENU_HAVE_CHILD = "permission.menu.haveChild";
        public static final String ERROR_COMPOSITE = "error.composite";
        public static final String SUCCESS_COMPOSITE = "success.composite";
        public static final String ERROR_SEND = "error.send";
        public static final String SUCCESS_SEND = "success.send";
        public static final String SUCCESS_SAVE = "success.save";
        public static final String DELETE_ERROR = "error.delete";
        public static final String SAVE_DUPLICATECODE = "save.duplicateCode";
        public static final String DOMAIN_DUPLICATECODE = "permission.duplicateDomain";
        public static final String EMP_CODE_DUPLICATECODE = "empCodeConfig.duplicatePrefixCode";
        public static final String SYNC_TAX_SUCCESS = "synctax.success";
        public static final String SYNC_TAX_ERROR = "synctax.error";
        public static final String CREATE_EMP_ID_ERROR = "create.emp.id.error";
        public static final String CREATE_ORG_ID_ERROR = "create.org.id.error";
        public static final String REWORK_FAIL = "transferEmployee.rework.Fail";
        public static final String REWORK_SUCCESS = "transferEmployee.rework.Success";
    }

    public static class WARNING_TYPE {

        public static final Long EMAIL = 0l;
        public static final Long SMS = 1l;
        public static final Long EMAIL_SMS = 2l;
    }


    public enum ProcessType {
        //Qua trinh trong
        INTERIOR(1L, "Interior"),
        //Quá trình ngoài
        EXTERIOR(2L, "Exterior"),
        //Qua trinh nha'p
        TYPE_DRAFT(3L, "Draft"),
        //la qua trinh hien tại
        STATUS_CURRENT(1L, "Current"),
        //ko phai qua trinh hien tai
        STATUS_NOT_CURRENT(null, "NotCurrent");
        private Long key;
        private String code;

        private ProcessType(Long key, String code) {
            this.key = key;
            this.code = code;
        }

        public Long getKey() {
            return key;
        }

        public String getCode() {
            return code;
        }
    }

    public interface COMMON {
        String FONT_FOLDER = CommonUtil.getConfig("fontFolder");
        String MARKET_COMPANY_ID = "MARKET_COMPANY_ID";
        String EXPORT_FOLDER = CommonUtil.getConfig("exportFolder");
        //Thu muc chua file tam de import
        String IMPORT_FOLDER = "/share/import/";
        String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
    }

    public enum attributeType {
        DATE1(1L, "dd/MM/yyyy"),
        DATE2(2L, "MM/dd/yyyy"),
        DATE3(3L, "yyyy/MM/dd"),
        DOUBLE(4L, "Double"),
        LONG(5L, "Long"),
        STRING(6L, "String"),
        DATE(7L, "Date");

        private Long key;
        private String code;

        private attributeType(Long key, String code) {
            this.key = key;
            this.code = code;
        }

        public Long getKey() {
            return key;
        }

        public String getCode() {
            return code;
        }
    }
}