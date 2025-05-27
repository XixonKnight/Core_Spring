package com.example.corespring.document.excel;


import com.example.corespring.common.JsonUtils;
import com.example.corespring.core.base.ColumnConfig;
import com.example.corespring.core.constants.Constants;
import com.example.corespring.core.enums.ErrorCodes;
import com.example.corespring.exceptions.ApplicationException;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
@Slf4j
public class ExcelUtils {
    private final String EXCEL_FILE_EXTENSION_XLSX = ".xlsx";
    private final String EXCEL_FILE_EXTENSION_XLS = ".xls";
    public final String EXCEL_FILE_MIME_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    private final String prefix = "columns";

    private static final String[] FORBIDDEN_KEY_WORDS = {
            "select", "insert", "update", "delete", "drop", "truncate",
            "exec", "execute", "script", "javascript", "alert"
    };

    private static final String[] FORBIDDEN_EXTENSIONS = {".exe", ".sh", ".bat", ".cmd", ".js"};

    // Hàm đọc file CFG và trả về các cột và yêu cầu, sử dụng try-catch để xử lý ngoại lệ
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

    public List<String> getHeadersMergeFromExcel(Workbook workbook, int sheetNumber, int headerRowCount) {
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
                String value = getMergedOrNormalCellValue(sheet, row, col, mergedRegions);
                if (StringUtils.isNotBlank(value)) {
                    parts.add(value.trim());
                }
            }

            headers.add(String.join(" - ", parts));
        }

        return headers;

    }

    public static List<String> validateExcelHeaders(List<String> excelHeaders, List<ColumnConfig> configs) {
        List<String> errors = new ArrayList<>();

        for (ColumnConfig config : configs) {
            String expectedHeader = String.join(" - ", config.getHeader());
            if (config.isRequired() && !excelHeaders.contains(expectedHeader)) {
                errors.add("Thiếu cột bắt buộc: " + expectedHeader + " (code: " + config.getCode() + ")");
            }
        }

        return errors;
    }

    //
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

    //
    private static String getMergedRegionCellValue(Sheet sheet, int rowIndex, int colIndex, List<CellRangeAddress> mergedRegions) {
        for (CellRangeAddress range : mergedRegions) {
            if (range.isInRange(rowIndex, colIndex)) {
                return getMergedCellValue(sheet, range, colIndex);
            }
        }
        return StringUtils.EMPTY;
    }

    //
    private static String getMergedCellValue(Sheet sheet, CellRangeAddress range, int colIndex) {
        Row firstRow = sheet.getRow(range.getFirstRow());
        if (firstRow == null) return "";

        Cell firstCell = firstRow.getCell(range.getFirstColumn());
        if (firstCell == null) return "";

        // Đảm bảo giá trị được lấy dưới dạng string
        String firstCellValue = getCellValue(firstCell);

        // Lấy giá trị của ô trong row tiếp theo (second row)
        Row secondRow = sheet.getRow(range.getFirstRow() + 1);
        if (secondRow != null) {
            Cell secondCell = secondRow.getCell(colIndex);
            String secondCellValue = getCellValue(secondCell);
            return firstCellValue + secondCellValue;
        }
        return firstCellValue;
    }

    //
    private String getNormalCellValue(Sheet sheet, int rowIndex, int colIndex) {
        Row row = sheet.getRow(rowIndex);
        if (row == null) return "";

        Cell cell = row.getCell(colIndex);
        if (cell == null) return "";
        return getCellValue(cell);
    }

    //
//    public static void validateFile(MultipartFile file) {
//
//        if (ObjectUtils.isEmpty(file) || !isFileNameSemanticallySafe(file.getOriginalFilename())) {
//            throw new AppException(ErrorCode.INVALID_FILE);
//        }
//
//        // Validate kích thước tệp
//        if (file.getSize() > Constants.FILE_MAX_SIZE) {
//            throw new AppException(ErrorCode.FILE_TOO_LARGE);
//        }
//
//        String fileName = file.getOriginalFilename();
//
//        // Validate định dạng tệp Excel
//        if (!StringUtils.isEmpty(fileName) && !fileName.endsWith(Constants.ExtensionExcel.XLSX) && !fileName.endsWith(Constants.ExtensionExcel.XLS)) {
//            throw new AppException(ErrorCode.INVALID_FILE_FORMAT);
//        }
//    }
//
//    /**
//     * Tạo Workbook từ tệp Excel (XLSX hoặc XLS).
//     *
//     * @param file Tệp Excel (MultipartFile)
//     * @return Workbook (XSSFWorkbook hoặc HSSFWorkbook)
//     * @throws AppException Nếu tệp không hợp lệ hoặc không thể đọc được
//     */
//    public static Workbook createWorkbook(MultipartFile file) {
//        validateFile(file); // Gọi hàm validate để kiểm tra file hợp lệ
//
//        try (InputStream inputStream = file.getInputStream()) {
//            String filename = file.getOriginalFilename();
//
//            // Kiểm tra định dạng và tạo workbook
//            if (filename != null && filename.endsWith(Constants.ExtensionExcel.XLSX)) {
//                return new XSSFWorkbook(inputStream);
//            } else if (filename != null && filename.endsWith(Constants.ExtensionExcel.XLS)) {
//                return new HSSFWorkbook(inputStream);
//            } else {
//                throw new AppException(ErrorCode.INVALID_FILE_FORMAT);
//            }
//        } catch (IOException e) {
//            log.error("Không thể đọc file Excel: {}", e.getMessage(), e);
//            throw new AppException(ErrorCode.INVALID_FILE);
//        } catch (Exception e) {
//            log.error("Lỗi trong khi xử lý file Excel: {}", e.getMessage(), e);
//            throw new AppException(ErrorCode.INVALID_FILE);
//        }
//    }
//
//    /**
//     * Kiểm tra header trong file Excel so với cấu hình yêu cầu
//     *
//     * @param pathConfig file config validation
//     * @param actualHeaders Danh sách các header thực tế từ file Excel
//     * @return Map<String, ColumnConfig> Cấu hình cột sau khi validate
//     * @throws AppException Nếu không tìm thấy cấu hình hoặc thiếu cột bắt buộc
//     */
//    public static Map<String, ColumnConfig> validateHeaderExcel(String pathConfig, List<String> actualHeaders) {
//        try {
//            // Load cấu hình từ file .cfg
//            Map<String, ColumnConfig> columnRules = ExcelReader.loadColumnRequirementsFromCfg(pathConfig);
//            if (ObjectUtils.isEmpty(columnRules)) {
//                log.error("Không tìm thấy file cấu hình kiểm tra header");
//                throw new AppException(ErrorCode.FILE_NOT_FOUND);
//            }
//
//            // So sánh số lượng cột header thực tế và cấu hình
//            if (actualHeaders.size() != columnRules.size()) {
//                log.error("Số lượng cột header không khớp với file cấu hình. Expected: {}, Actual: {}", columnRules.size(), actualHeaders.size());
//                throw new AppException(ErrorCode.INVALID_HEADER_FORMAT);
//            }
//
//            // Kiểm tra các cột bắt buộc có mặt trong file Excel
//            for (Map.Entry<String, ColumnConfig> entry : columnRules.entrySet()) {
//                boolean isRequired = entry.getValue().isRequired();
//                String expectedColumn = entry.getKey();
//
//                if (isRequired && !actualHeaders.contains(expectedColumn)) {
//                    log.error("Thiếu cột bắt buộc: {}", expectedColumn);
//                    throw new AppException(ErrorCode.INVALID_HEADER_FORMAT);
//                }
//            }
//
//
//            return columnRules;
//
//        }catch (AppException ae) {
//            // Ném lại AppException với error code cụ thể
//            throw ae;
//        }catch (Exception e) {
//            log.error("Lỗi khi validate header Excel", e);
//            throw new AppException(ErrorCode.SYSTEM_ERROR);
//        }
//    }
//
//    public static List<Map<String, String>> readDataExcel(Workbook workbook, Map<String, ColumnConfig> headerRules,
//                                                          List<String> excelHeaders, int sheetNumber, int rowStartRead) {
//        List<Map<String, String>> results = new ArrayList<>();
//        Sheet sheet = workbook.getSheetAt(sheetNumber);
//        int rowCount = sheet.getPhysicalNumberOfRows();
//
//        // Kiểm tra dữ liệu có tồn tại không
//        if (rowCount <= rowStartRead) {
//            throw new AppException(ErrorCode.INVALID_FILE_FORMAT);
//        }
//
//        // Ánh xạ: chỉ số cột trong Excel -> thông tin cấu hình cột
//        Map<Integer, ColumnConfig> columnIndexToKey = new HashMap<>();
//        for (int i = 0; i < excelHeaders.size(); i++) {
//            String header = excelHeaders.get(i);
//            if (headerRules.containsKey(header)) {
//                ColumnConfig cfg = headerRules.get(header);
//                columnIndexToKey.put(i, ColumnConfig.builder()
//                        .name(header)
//                        .required(cfg.isRequired())
//                        .build());
//            }
//        }
//
//        //Bắt đầu đọc dữ liệu
//        for (int i = rowStartRead; i < rowCount; i++) {
//            Row row = sheet.getRow(i);
//            if (row == null || isRowEmpty(row)) break;
//
//            Map<String, String> rowData = new LinkedHashMap<>();
//            for (Map.Entry<Integer, ColumnConfig> entry : columnIndexToKey.entrySet()) {
//                int columnIndex = entry.getKey();
//                ColumnConfig columnConfig = entry.getValue();
//
//                Cell cell = row.getCell(columnIndex);
//                String value = getCellValue(cell);
//
//                if (columnConfig.isRequired() && StringUtils.isBlank(value)) {
//                    throw new AppException(ErrorCode.INVALID_INPUT_DATA_REQUIRED);
//                }
//
//                String key = headerRules.get(columnConfig.getName()).getName();
//                rowData.put(key, sanitize(value));
//            }
//            results.add(rowData);
//        }
//
//        return results;
//
//    }
//
//    private boolean isRowEmpty(Row row) {
//        for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
//            Cell cell = row.getCell(j);
//            if (cell != null && !cell.toString().trim().isEmpty()) {
//                return false; // Dòng có ít nhất một ô có dữ liệu
//            }
//        }
//        return true; // Dòng không có ô nào có dữ liệu
//    }
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
                    return String.valueOf(cell.getNumericCellValue());
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
                            return String.valueOf(evaluatedValue.getNumberValue());
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
//    /**
//     * Kiểm tra tên file có  hợp lệ hay không
//     * @param filename tên file
//     * @return true: Hợp lệ | false: không hợp lệ
//     */
//    // Kiểm tra tên file hợp lệ (tránh script, ký tự đặc biệt)
//    public static boolean isFileNameSemanticallySafe(String filename) {
//        if (filename == null) return false;
//
//        // Chuyển về lowercase để so sánh từ khóa
//        String lower = filename.toLowerCase();
//
//        for (String keyword : FORBIDDEN_KEY_WORDS) {
//            if (keyword.contains(lower)) {
//                return false;
//            }
//        }
//
//        // Chặn path traversal
//        if (lower.contains("..") || lower.contains("/") || lower.contains("\\")) return false;
//
//        // Chặn biến môi trường hoặc ký tự shell
//        if (lower.contains("%") || lower.contains("$") || lower.contains("system32")) return false;
//
//        for (String ext : FORBIDDEN_EXTENSIONS) {
//            if (lower.endsWith(ext)) return false;
//        }
//
//        // Cho phép ký tự hợp lệ cơ bản
//        return filename.matches("^[\\w\\-. ]+$");
//    }
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
