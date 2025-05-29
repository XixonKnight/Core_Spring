package com.example.corespring.offices.excels;


import com.example.corespring.utils.CommonUtils;
import com.example.corespring.utils.DateUtils;
import com.example.corespring.utils.JsonUtils;
import com.example.corespring.core.constants.Constants;
import com.example.corespring.core.enums.ErrorCodes;
import com.example.corespring.exceptions.ApplicationException;
import com.example.corespring.utils.VietnameseTextUtils;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.apache.commons.compress.utils.ArchiveUtils.sanitize;

@UtilityClass
@Slf4j
public class ExcelUtils {
    private final String EXCEL_FILE_EXTENSION_XLSX = ".xlsx";
    private final String EXCEL_FILE_EXTENSION_XLS = ".xls";
    public final String EXCEL_FILE_MIME_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";


    private static final String[] FORBIDDEN_KEY_WORDS = {
            "select", "insert", "update", "delete", "drop", "truncate",
            "exec", "execute", "script", "javascript", "alert"
    };

    private static final String[] FORBIDDEN_EXTENSIONS = {".exe", ".sh", ".bat", ".cmd", ".js"};

    /**
     * Hàm đọc file yaml và trả về các cột và yêu cầu, sử dụng try-catch để xử lý ngoại lệ
     * @param yamlPath đường dẫn file config
     * @return List of {@link ColumnConfig} Danh sách các cột cấu hình
     */
    public static List<ColumnConfig> loadColumnRequirementsFromCfg(String yamlPath) {
        LoaderOptions options = new LoaderOptions();
        options.setAllowDuplicateKeys(false);
        options.setAllowRecursiveKeys(false);
        Constructor constructor = new Constructor(List.class, options);
        Yaml yaml = new Yaml(constructor);

        try (InputStream is = new ClassPathResource(yamlPath).getInputStream()) {
            // Cast đúng kiểu cần thiết
            return JsonUtils.convertList(yaml.loadAs(is, List.class), ColumnConfig.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load column config", e);
        }

    }

    /**
     * Tạo Workbook từ tệp Excel (XLSX hoặc XLS).
     *
     * @param file Tệp Excel (MultipartFile)
     * @return Workbook (XSSFWorkbook hoặc HSSFWorkbook)
     * @throws ApplicationException Nếu tệp không hợp lệ hoặc không thể đọc được
     */
    public Workbook createWorkbook(MultipartFile file) {
//        validateFile(file); // Gọi hàm validate để kiểm tra file hợp lệ

        try (InputStream inputStream = file.getInputStream()) {
            String filename = file.getOriginalFilename();

            // Kiểm tra định dạng và tạo workbook
            if (filename != null && filename.endsWith(EXCEL_FILE_EXTENSION_XLSX)) {
                return new XSSFWorkbook(inputStream);
            } else if (filename != null && filename.endsWith(EXCEL_FILE_EXTENSION_XLS)) {
                return new HSSFWorkbook(inputStream);
            } else {
                throw new ApplicationException(ErrorCodes.INVALID_FILE_FORMAT);
            }
        } catch (IOException e) {
            log.error("Không thể đọc file Excel: {}", e.getMessage(), e);
            throw new ApplicationException(ErrorCodes.INVALID_FILE);
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            log.error("Lỗi trong khi xử lý file Excel: {}", e.getMessage(), e);
            throw new ApplicationException(ErrorCodes.INVALID_FILE);
        }
    }

    /**
     * Lấy danh sách header có trong file excel
     * @param workbook Workbook
     * @param sheetNumber sheet cần lấy
     * @param headerRowCount số dòng chứa header
     * @return Danh sách header trong file excel
     */
//    public List<String> getHeadersMergeFromExcel(Workbook workbook, int sheetNumber, int headerRowCount) {
//        List<String> headers = new ArrayList<>();
//
//        Sheet sheet = workbook.getSheetAt(sheetNumber);
//        List<CellRangeAddress> mergedRegions = sheet.getMergedRegions();
//
//        // Tính số cột lớn nhất trong các dòng header
//        int maxCol = 0;
//        for (int i = 0; i < headerRowCount; i++) {
//            Row row = sheet.getRow(i);
//            if (row != null && row.getLastCellNum() > maxCol) {
//                maxCol = row.getLastCellNum();
//            }
//        }
//
//        // Duyệt từng cột
//        for (int col = 0; col < maxCol; col++) {
//            List<String> parts = new ArrayList<>();
//
//            // Duyệt từng dòng trong header
//            for (int row = 0; row < headerRowCount; row++) {
//                String value = getMergedOrNormalCellValue(sheet, row, col, mergedRegions);
//                if (StringUtils.isNotBlank(value) && parts.stream().noneMatch(s -> VietnameseTextUtils.removeAccent(s).equalsIgnoreCase(VietnameseTextUtils.removeAccent(value)))) {
//                    parts.add(value.trim());
//                }
//            }
//            headers.add(VietnameseTextUtils.removeAccent(String.join(Constants.Key.DASH, parts)));
//        }
//
//        return headers;
//
//    }

    /**
     * Lấy danh sách header có trong file excel
     * @param workbook Workbook
     * @param sheetNumber sheet cần lấy
     * @param headerRowCount số dòng chứa header
     * @param isHeaderMerged header có cột merge không
     * @return Danh sách header trong file excel
     */
    public List<String> getHeaderExcel(Workbook workbook, int sheetNumber, int headerRowCount, boolean isHeaderMerged) {
        List<String> headers = new ArrayList<>();

        Sheet sheet = workbook.getSheetAt(sheetNumber);
        List<CellRangeAddress> mergedRegions = sheet.getMergedRegions();

        // Tính số cột lớn nhất trong các dòng header
        int maxCol = 0;
        for (int i = 0; i < headerRowCount; i++) {
            Row row = sheet.getRow(i);
            if (row != null && row.getLastCellNum() > maxCol) {
                maxCol = row.getLastCellNum();
            }
        }

        // Duyệt từng cột
        for (int col = 0; col < maxCol; col++) {
            List<String> parts = new ArrayList<>();

            // Duyệt từng dòng trong header
            for (int row = 0; row < headerRowCount; row++) {
                String value = isHeaderMerged ? getMergedOrNormalCellValue(sheet, row, col, mergedRegions) : Objects.requireNonNull(getCell(sheet, row, col)).getStringCellValue();
                if (StringUtils.isNotBlank(value) && parts.stream().noneMatch(s -> VietnameseTextUtils.removeAccent(s).equalsIgnoreCase(VietnameseTextUtils.removeAccent(value)))) {
                    parts.add(value.trim());
                }
            }
            headers.add(VietnameseTextUtils.removeAccent(String.join(Constants.Key.DASH, parts)));
        }

        return headers;

    }


    /**
     * Validate header trong file với file yaml đã cấu hình
     * @param excelHeaders Header trong file excel
     * @param configs Header trong file yaml đã cấu hình
     * @return Danh sách lỗi cột không match
     */
    public static List<String> validateExcelHeaders(List<String> excelHeaders, List<ColumnConfig> configs) {
        List<String> errors = new ArrayList<>();

        if (excelHeaders.size() != configs.size()) {
            throw new IllegalArgumentException(
                    "Số cột trong Excel (" + excelHeaders.size() +
                            ") không khớp với số cấu hình (" + configs.size() + ")."
            );
        }

        for (ColumnConfig config : configs) {
            config.setHeaderJoins(VietnameseTextUtils.removeAccent(String.join(Constants.Key.DASH, config.getHeaders())));
            if (config.isRequired() && !excelHeaders.contains(config.getHeaderJoins())) {
                errors.add("Thiếu cột bắt buộc: " + config.getHeaderJoins());
            }
        }

        return errors;
    }

    private String getMergedOrNormalCellValue(Sheet sheet, int rowIndex, int colIndex, List<CellRangeAddress> mergedRegions) {
        for (CellRangeAddress range : mergedRegions) {
            if (range.isInRange(rowIndex, colIndex)) {
                Cell mergedCell = getCell(sheet, range.getFirstRow(), range.getFirstColumn());
                return getCellValue(mergedCell);
            }
        }
        Cell cell = getCell(sheet, rowIndex, colIndex);
        return getCellValue(cell);
    }

    private Cell getCell(Sheet sheet, int rowIdx, int colIdx) {
        Row row = sheet.getRow(rowIdx);
        if (row == null) return null;
        return row.getCell(colIdx);
    }

    /**
     * Đọc dữ liệu file excel
     * @param config {@link ExcelReadConfig}
     * @return List of object
     */
    public static <T> List<T> readDataExcel(ExcelReadConfig<T> config) {

        Workbook workbook = config.getWorkbook();
        int sheetNumber = config.getSheetNumber();
        int rowStartRead = config.getRowStartRead();
        List<String> excelHeaders = config.getExcelHeaders();
        List<ColumnConfig> headerConfig = config.getHeaderConfig();
        Class<T> targetClass = config.getTargetClass();

        //Lấy thông tin sheet
        Sheet sheet = workbook.getSheetAt(sheetNumber);

        int rowCount = sheet.getPhysicalNumberOfRows();

        //Dòng bắt đầu đọc dữ liệu
        if (rowCount <= rowStartRead) {
            throw new ApplicationException(ErrorCodes.INVALID_FILE_FORMAT);
        }

        // Map: Excel column index -> ColumnConfig
        Map<Integer, ColumnConfig> columnIndexToConfig = new LinkedHashMap<>();
        Map<String, ColumnConfig> headerNameToConfig = headerConfig.stream()
                .collect(Collectors.toMap(ColumnConfig::getHeaderJoins, Function.identity()));

        //Map idx cột để check validate dữ liệu
        for (int i = 0; i < excelHeaders.size(); i++) {
            String header = excelHeaders.get(i);
            ColumnConfig colCfg = headerNameToConfig.get(header);
            if (colCfg != null) {
                columnIndexToConfig.put(i, colCfg);
            }
        }

        List<T> resultList = new ArrayList<>();

        //Bắt đầu đọc dữ liệu
        for (int i = rowStartRead; i < rowCount; i++) {
            Row row = sheet.getRow(i);
            if (row == null || isRowEmpty(row)) break;

            Map<String, String> rowData = new LinkedHashMap<>();

            for (Map.Entry<Integer, ColumnConfig> entry : columnIndexToConfig.entrySet()) {
                int colIndex = entry.getKey();
                ColumnConfig colCfg = entry.getValue();

                Cell cell = row.getCell(colIndex);
                String rawValue = getCellValue(cell);

                if (colCfg.isRequired() && StringUtils.isBlank(rawValue)) {
                    throw new ApplicationException(ErrorCodes.INVALID_INPUT_DATA_REQUIRED);
                }

                if (parseValueByType(rawValue, colCfg.getType(), Constants.FormatDate.PATTERN_YYYY_MM_DD) == null) {
                    log.error("Parse error - value: {}, type: {}, column: {}", rawValue, colCfg.getType(), colCfg.getHeaderJoins());
                    throw new ApplicationException(ErrorCodes.SYSTEM_ERROR);
                }

                rowData.put(colCfg.getCode(), sanitize(rawValue));
            }

            resultList.add(JsonUtils.mapToObject(rowData, targetClass));
        }

        return resultList;
    }

    public Object parseValueByType(String value, String type, String patternDate) {
        if (!CommonUtils.hasText(value)) return "";

        try {
            return switch (type) {
                case TypeFields.BigDecimal -> new BigDecimal(value.replace(",", "").trim());
                case TypeFields.Integer -> Integer.valueOf(value.trim());
                case TypeFields.Long -> Long.valueOf(value.trim());
                case TypeFields.Double -> Double.valueOf(value.trim());
                case TypeFields.LocalDate -> DateUtils.stringToLocalDate(value, patternDate); // format tùy chỉnh
                case TypeFields.Boolean -> parseBoolean(value); // format tùy chỉnh
                default -> value;
            };
        } catch (Exception e) {
            log.error("[Exception] parseValueByType value: {}, type: {}, patternDate: {}", value, type, patternDate, e);
            return null;
        }
    }

    @UtilityClass
    private class TypeFields {
        public final String BigDecimal = "BigDecimal";
        public final String Integer = "Integer";
        public final String Long = "Long";
        public final String Double = "Double";
        public final String LocalDate = "LocalDate";
        public final String Boolean = "Boolean";
    }

    private Boolean parseBoolean(String value) {
        value = value.toLowerCase();
        return switch (value) {
            case "true", "1", "yes", "có", "x", "✓" -> true;
            case "false", "0", "no", "không", "ko", "" -> false;
            default -> null;
        };
    }

    //
    public static void validateFile(MultipartFile file) {

        if (CommonUtils.isEmpty(file) || !isFileNameSemanticallySafe(file.getOriginalFilename())) {
            throw new ApplicationException(ErrorCodes.INVALID_FILE);
        }

        // Validate kích thước tệp
//        if (file.getSize() > Constants.FILE_MAX_SIZE) {
//            throw new AppException(ErrorCode.FILE_TOO_LARGE);
//        }

        String fileName = file.getOriginalFilename();

        // Validate định dạng tệp Excel
        if (!StringUtils.isEmpty(fileName) && !fileName.endsWith(EXCEL_FILE_EXTENSION_XLSX) && !fileName.endsWith(EXCEL_FILE_EXTENSION_XLS)) {
            throw new ApplicationException(ErrorCodes.INVALID_FILE_FORMAT);
        }
    }

    private boolean isRowEmpty(Row row) {
        for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
            Cell cell = row.getCell(j);
            if (cell != null && !cell.toString().trim().isEmpty()) {
                return false; // Dòng có ít nhất một ô có dữ liệu
            }
        }
        return true; // Dòng không có ô nào có dữ liệu
    }
//
    public String getCellValue(Cell cell) {
        if (cell == null) {
            return StringUtils.EMPTY;
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return new SimpleDateFormat(Constants.FormatDate.PATTERN_YYYY_MM_DD).format(cell.getDateCellValue());
                } else {
                    // Format để tránh hiển thị dạng 1.0E7
                    DecimalFormat df = new DecimalFormat("#");
                    return df.format(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
                CellValue evaluatedValue = evaluator.evaluate(cell);
                if (evaluatedValue == null) return StringUtils.EMPTY;

                switch (evaluatedValue.getCellType()) {
                    case STRING:
                        return evaluatedValue.getStringValue();
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(cell)) {
                            return new SimpleDateFormat(Constants.FormatDate.PATTERN_YYYY_MM_DD).format(cell.getDateCellValue());
                        } else {
                            DecimalFormat df = new DecimalFormat("#");
                            return df.format(evaluatedValue.getNumberValue());
                        }
                    case BOOLEAN:
                        return String.valueOf(evaluatedValue.getBooleanValue());
                    default:
                        return StringUtils.EMPTY;
                }
            default:
                return StringUtils.EMPTY;
        }
    }

//    public static CellStyle createBorderedCellStyle(Workbook workbook) {
//        CellStyle style = workbook.createCellStyle();
//
//        style.setBorderBottom(BorderStyle.THIN);
//        style.setBorderTop(BorderStyle.THIN);
//        style.setBorderLeft(BorderStyle.THIN);
//        style.setBorderRight(BorderStyle.THIN);
//
//        return style;
//    }
//
//    public static void createCell(Row row, int columnIndex, Object value, CellStyle style) {
//        Cell cell = row.createCell(columnIndex);
//
//        if (value instanceof Number number) {
//            cell.setCellValue(number.doubleValue());
//        } else if (value instanceof Boolean bool) {
//            cell.setCellValue(bool);
//        } else if (value instanceof LocalDate localDate) {
//            cell.setCellValue(java.sql.Date.valueOf(localDate));
//        } else if (value != null) {
//            cell.setCellValue(value.toString());
//        } else {
//            cell.setBlank();
//        }
//
//        if (style != null) {
//            cell.setCellStyle(style);
//        }
//    }
//
    /**
     * Kiểm tra tên file có  hợp lệ hay không
     * @param filename tên file
     * @return true: Hợp lệ | false: không hợp lệ
     */
    // Kiểm tra tên file hợp lệ (tránh script, ký tự đặc biệt)
    public static boolean isFileNameSemanticallySafe(String filename) {
        if (filename == null) return false;

        // Chuyển về lowercase để so sánh từ khóa
        String lower = filename.toLowerCase();

        for (String keyword : FORBIDDEN_KEY_WORDS) {
            if (keyword.contains(lower)) {
                return false;
            }
        }

        // Chặn path traversal
        if (lower.contains("..") || lower.contains("/") || lower.contains("\\")) return false;

        // Chặn biến môi trường hoặc ký tự shell
        if (lower.contains("%") || lower.contains("$") || lower.contains("system32")) return false;

        for (String ext : FORBIDDEN_EXTENSIONS) {
            if (lower.endsWith(ext)) return false;
        }

        // Cho phép ký tự hợp lệ cơ bản
        return filename.matches("^[\\w\\-. ]+$");
    }
//
//
//    /**
//     * Clean data trong file excel trước khi xử lý với database
//     * @param value
//     * @return
//     */
//    private static String sanitize(String value) {
//        if (value == null) return "";
//
//        String sanitized = value.trim();
//
//        // Loại bỏ script, javascript, HTML tags nguy hiểm (XSS)
//        sanitized = sanitized.replaceAll("(?i)<script.*?>.*?</script>", ""); // loại bỏ script tag
//        sanitized = sanitized.replaceAll("(?i)<.*?javascript:.*?>.*?</.*?>", ""); // loại bỏ javascript:
//        sanitized = sanitized.replaceAll("(?i)<.*?>", ""); // loại bỏ thẻ HTML nói chung
//
//        // Loại bỏ ký tự SQL injection phổ biến
//        sanitized = sanitized.replaceAll("[\"'`;]", "");
//
//        // Giới hạn độ dài tối đa
//        if (sanitized.length() > 255) {
//            sanitized = sanitized.substring(0, 255);
//        }
//
//        return sanitized;
//    }

}
