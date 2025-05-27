package com.example.corespring.common;

import com.example.corespring.core.base.SearchParams;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.util.Pair;
import org.slf4j.MDC;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.sql.PreparedStatement;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

@Slf4j
@UtilityClass
public class CommonUtils {

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final String UNICODE = "ăâđêôơưàảãạáằẳẵặắầẩẫậấèẻẽẹéềểễệếìỉĩịíòỏõọóồổỗộốờởỡợớùủũụúừửữựứỳỷỹỵý";

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>)
                    (src, typeOfSrc, context) -> new JsonPrimitive(src.format(DateTimeFormatter.ISO_DATE)))
            .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>)
                    (src, typeOfSrc, context) -> new JsonPrimitive(src.format(DateTimeFormatter.ISO_DATE_TIME)))
            .registerTypeAdapter(ZonedDateTime.class, (JsonSerializer<ZonedDateTime>)
                    (src, typeOfSrc, context) -> new JsonPrimitive(src.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)))
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create();
    /**
     * Cast BigDemical to Long
     *
     * @param value
     * @return
     */
    public static Long demicalToLong(BigDecimal value) {
        if (value == null) {
            return 0L;
        } else {
            return value.longValue();
        }
    }

    /**
     * Check list object is null.
     *
     * @param data
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static boolean isNullOrEmpty(List data) {
        return (data == null || data.isEmpty());
    }

    /**
     * Lay gia tri tu file config.properties.
     *
     * @param key Khoa
     * @return Gia tri
     */
    public static String getConfig(String key) {
        ResourceBundle rb = ResourceBundle.getBundle("config");
        return rb.getString(key);
    }

    /**
     * Lay gia tri tu file don_gia_ttbl.properties.
     *
     * @param key Khoa
     * @return Gia tri
     */
    public static String getXNKConfig(String key) {
        ResourceBundle rb = ResourceBundle.getBundle("xnk_ttbl_config");
        return rb.getString(key);
    }

    /**
     * Lay gia tri tu file permissionCode.properties.
     *
     * @param key Khoa
     * @return Gia tri
     */
    public static String getPermissionCode(String key) {
        ResourceBundle rb = ResourceBundle.getBundle("permissionCode");
        return rb.getString(key);
    }

    /**
     * Lay xau gia tri tu file ApplicationResources.properties.
     *
     * @param key Khoa
     * @return Gia tri
     */
    public static String getApplicationResource(String key) {
        try {
            ResourceBundle rb = ResourceBundle.getBundle("ApplicationResources", new Locale("vi"));
            return rb.getString(key);
        } catch (Exception ex) {
            log.error("getApplicationResource:", ex);
            return "";
        }

    }

    /**
     * Lay xau gia tri tu file ApplicationResources.properties.
     *
     * @param key Khoa
     * @return Gia tri
     */
    public static String getApplicationResource(String key, Object... args) {
        try {
            ResourceBundle rb = ResourceBundle.getBundle("ApplicationResources", new Locale("vi"));
            return String.format(rb.getString(key), args);
        } catch (Exception ex) {
            log.error("getApplicationResource:", ex);
            return "";
        }

    }

    public static Object NVL(Object value, Object defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static Double NVL(Double value) {

        return NVL(value, 0D);
    }

    public static Integer NVL(Integer value) {
        return value == null ? 0 : value;
    }

    public static Integer NVL(Integer value, Integer defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static BigDecimal NVL(BigDecimal value) {
        return value == null ? new BigDecimal(0) : value;
    }

    public static Double NVL(Double value, Double defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static Long NVL(Long value, Long defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static String NVL(String value, String nullValue, String notNullValue) {

        return value == null ? nullValue : notNullValue;
    }

    public static String NVL(String value, String defaultValue) {

        return NVL(value, defaultValue, value);
    }

    public static String NVL(String value) {

        return NVL(value, "");
    }

    public static Long NVL(Long value) {

        return NVL(value, 0L);
    }

    public static Long checkBoxValue(Long value) {
        if (value != null && value.equals(1L)) {
            return 1L;
        } else {
            return 0L;
        }
    }

    public static String getClientIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-FORWARDED-FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * Convert String to list
     *
     * @param sourceString
     * @param pattern
     * @return
     */
    public static List<String> toList(String sourceString, String pattern) {
        List<String> results = new LinkedList<String>();
        if (NVL(sourceString).trim().isEmpty()) {
            return results;
        }
        String[] sources = NVL(sourceString).split(pattern);
        for (String source : sources) {
            results.add(NVL(source).trim());
        }
        return results;
    }

    /**
     * Hàm lấy ngày đầu tháng và cuối tháng
     *
     * @param month Month
     * @return Pair<LocalDate, LocalDate>
     */
    public static Pair<LocalDate, LocalDate> getFirstAndLastDayOfMonth(int month, int year) {

        LocalDate firstDay = LocalDate.of(year, month, 1);
        LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());

        return Pair.create(firstDay, lastDay);
    }

    /**
     * Convert image to base64 code.
     *
     * @param imageFile
     * @return
     * @throws IOException
     */
    public static String getBase64StringOfImage(File imageFile) throws IOException {
        String imgString;
        BufferedImage buffImage = ImageIO.read(imageFile);
        try (ByteArrayOutputStream bout = new ByteArrayOutputStream()) {
            ImageIO.write(buffImage, "jpg", bout);
            byte[] imageBytes = bout.toByteArray();
            imgString = Base64.getEncoder().encodeToString(imageBytes);
        }
        return imgString;
    }

    /**
     * Bypass HHTPS
     *
     * @throws Exception
     */
    public static void disableSslVerification() throws Exception {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};

        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }


    /**
     * kiem tra 1 xau rong hay null khong
     *
     * @param str         : xau
     * @param queryString
     * @param paramList
     * @param field
     */
    public static void filter(String str, StringBuilder queryString, List<Object> paramList, String field) {
        if ((str != null) && !"".equals(str.trim())) {
            queryString.append(" AND LOWER(").append(field).append(") LIKE ? ESCAPE '/'");
            str = str.replace("  ", " ");
            str = "%" + str.trim().toLowerCase().replace("/", "//").replace("_", "/_").replace("%", "/%") + "%";
            paramList.add(str);
        }
    }

    /**
     * kiem tra 1 so rong hay null khong
     *
     * @param n           So
     * @param queryString
     * @param paramList
     * @param field
     */
    public static void filter(Long n, StringBuilder queryString, List<Object> paramList, String field) {
        if ((n != null) && (n > 0L)) {
            queryString.append(" AND ").append(field).append(" = ? ");
            paramList.add(n);
        }
    }

    /**
     * kiem tra 1 so rong hay null khong
     *
     * @param n           So
     * @param queryString
     * @param paramList
     * @param field
     */
    public static void filter(Double n, StringBuilder queryString, List<Object> paramList, String field) {
        if ((n != null) && (n > 0L)) {
            queryString.append(" AND ").append(field).append(" = ? ");
            paramList.add(n);
        }
    }

    /**
     * kiem tra 1 so rong hay null khong
     *
     * @param n           So
     * @param queryString
     * @param paramList
     * @param field
     */
    public static void filter(Boolean n, StringBuilder queryString, List<Object> paramList, String field) {
        if (n != null) {
            queryString.append(" AND ").append(field).append(" = ? ");
            paramList.add(n);
        }
    }

    /**
     * kiem tra 1 Integer rong hay null khong
     *
     * @param n           So
     * @param queryString
     * @param paramList
     * @param field
     */
    public static void filter(Integer n, StringBuilder queryString, List<Object> paramList, String field) {
        if ((n != null) && (n > 0)) {
            queryString.append(" AND ").append(field).append(" = ? ");
            paramList.add(n);
        }
    }

    /**
     * kiem tra 1 xau rong hay null khong
     *
     * @param date        So
     * @param queryString
     * @param field
     * @param paramList
     */
    public static void filter(Date date, StringBuilder queryString, List<Object> paramList, String field) {
        if ((date != null)) {
            queryString.append(" AND ").append(field).append(" = ? ");
            paramList.add(date);
        }
    }

    /**
     * kiem tra 1 xau rong hay null khong
     *
     * @param arrIds
     * @param queryString
     * @param paramList
     * @param field
     */
    public static void filterSelectInL(String arrIds, StringBuilder queryString, List<Object> paramList, String field) {
        if (hasText(arrIds)) {
            queryString.append(" AND ").append(field).append(" IN (-1 ");
            String[] ids = arrIds.split(",");
            for (String strId : ids) {
                queryString.append(", ?");
                paramList.add(Long.parseLong(strId.trim()));
            }
            queryString.append(" ) ");
        }
    }

    public static boolean hasText(String str) {
        return str != null && !str.isEmpty() && containsText(str);
    }

    private static boolean containsText(CharSequence str) {
        int strLen = str.length();

        for (int i = 0; i < strLen; ++i) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }

        return false;
    }

    /**
     * Kiem tra lon hon hoac bang.
     *
     * @param obj         So
     * @param queryString
     * @param paramList
     * @param field
     */
    public static void filterGe(Object obj, StringBuilder queryString, List<Object> paramList, String field) {
        if (obj != null && !"".equals(obj)) {
            queryString.append(" AND ").append(field).append(" >= ? ");
            paramList.add(obj);
        }
    }

    /**
     * Kiem tra nho hon hoac bang.
     *
     * @param obj         So
     * @param queryString
     * @param paramList
     * @param field
     */
    public static void filterLe(Object obj, StringBuilder queryString, List<Object> paramList, String field) {
        if (obj != null && !"".equals(obj)) {
            queryString.append(" AND ").append(field).append(" <= ? ");
            paramList.add(obj);
        }
    }

    /**
     * filter for inserting preparedStatement
     *
     * @param value
     * @param index
     * @param preparedStatement
     * @throws Exception
     */
    public static void filter(String value, PreparedStatement preparedStatement, int index) throws Exception {

        if (value != null) {
            preparedStatement.setString(index, value.trim());
        } else {
            preparedStatement.setNull(index, java.sql.Types.NULL);
        }
    }

    /**
     * @param value
     * @param preparedStatement
     * @param index
     * @throws Exception
     */
    public static void filter(Double value, PreparedStatement preparedStatement, int index) throws Exception {

        if (value != null) {
            preparedStatement.setDouble(index, value);
        } else {
            preparedStatement.setNull(index, java.sql.Types.NULL);
        }
    }

    /**
     * @param value
     * @param preparedStatement
     * @param index
     * @throws Exception
     */
    public static void filter(Long value, PreparedStatement preparedStatement, int index) throws Exception {

        if (value != null) {
            preparedStatement.setLong(index, value);
        } else {
            preparedStatement.setNull(index, java.sql.Types.NULL);
        }
    }

    /**
     * @param value
     * @param preparedStatement
     * @param index
     * @throws Exception
     */
    public static void filter(Object value, PreparedStatement preparedStatement, int index) throws Exception {
        if (value != null) {
            if (value instanceof Date temp) {
                preparedStatement.setObject(index, new java.sql.Timestamp(temp.getTime()));
            } else {
                preparedStatement.setObject(index, value);
            }

        } else {
            preparedStatement.setNull(index, java.sql.Types.NULL);
        }
    }

    /**
     * @param value
     * @param preparedStatement
     * @param index
     * @throws Exception
     */
    public static void filter(java.sql.Date value, PreparedStatement preparedStatement, int index) throws Exception {

        if (value != null) {
            preparedStatement.setDate(index, value);
        } else {
            preparedStatement.setNull(index, java.sql.Types.NULL);
        }
    }

    /**
     * kiem tra mot chuoi co chua ky tu Unicode khong
     *
     * @param str
     * @return
     */
    public static boolean containUnicode(String str) {
        if ("".equals(str)) {
            return false;
        } else {
            int count = 0;
            String subStr;
            while (count < str.length()) {
                subStr = str.substring(count, count + 1);
                if (UNICODE.contains(subStr)) {
                    return true;
                }
                count++;
            }
        }
        return false;
    }

    /**
     * kiem tra mot chuoi co chua ky tu Unicode khong
     *
     * @param str
     * @return
     */
    public static boolean containPhoneNumber(String str) {
        return !hasText(str) && str.matches(".*\\d.*");
    }

    /**
     * replaceSpecialKeys
     *
     * @param str String
     * @return String
     */
    public static String replaceSpecialKeys(String str) {
        str = str.replace("  ", " ");
        str = "%" + str.trim().toLowerCase().replace("/", "//").replace("_", "/_").replace("%", "/%") + "%";
        return str;
    }

    /**
     * Format so.
     *
     * @param d So
     * @return Xau
     */
    public static String formatNumber(Double d) {
        if (d == null) {
            return "";
        } else {
            DecimalFormat format = new DecimalFormat("######.#####");
            return format.format(d);
        }
    }

    /**
     * Format so.
     *
     * @param d So
     * @return Xau
     */
    public static String formatNumber(Long d) {
        if (d == null) {
            return "";
        } else {
            DecimalFormat format = new DecimalFormat("######");
            return format.format(d);
        }
    }

    /**
     * Format so.
     *
     * @param d      So
     * @param pattern
     * @return Xau
     */
    public static String formatNumber(Object d, String pattern) {
        if (d == null) {
            return "";
        } else {
            DecimalFormat format = new DecimalFormat(pattern);
            return format.format(d);
        }
    }

    /**
     * chuyen list string ve chuỗi phuc vu tim kiem
     *
     * @param lstObject lst
     * @param separator
     * @return ket qua
     * @throws Exception ex
     */
    @SuppressWarnings("rawtypes")
    public static String convertListToString(List lstObject, String separator) throws Exception {
        try {
            if (!isNullOrEmpty(lstObject)) {
                StringBuilder result = new StringBuilder();
                int size = lstObject.size();
                result.append("'").append(lstObject.get(0)).append("'");
                for (int i = 1; i < size; i++) {
                    result.append(separator);
                    result.append("'");
                    result.append(lstObject.get(i));
                    result.append("'");
                }
                return result.toString();
            } else {
                return "";
            }
        } catch (Exception e) {
            log.error("convertListToString:", e);
            return "";
        }
    }
    /**
     * Convert Object To String Json
     *
     * @param object Object cần convert
     * @return String
     */
    public static String objectToJson(Object object) {
        String strMess = "";
        try {
            strMess = gson.toJson(object);
        } catch (Exception e) {
            log.error("Error! Convert object to json", e);
        }
        return strMess;
    }


    /**
     * Checks if is collection empty.
     *
     * @param collection the collection
     * @return true, if is collection empty
     */
    private static boolean isCollectionEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return false;
        } else if (obj instanceof Optional) {
            return ((Optional<?>) obj).isPresent();
        } else if (obj instanceof CharSequence) {
            return !((CharSequence) obj).isEmpty();
        } else if (obj.getClass().isArray()) {
            return Array.getLength(obj) != 0;
        } else if (obj instanceof Collection) {
            return !((Collection<?>) obj).isEmpty();
        } else {
            return !(obj instanceof Map) || !((Map<?, ?>) obj).isEmpty();
        }
    }

    /**
     * Builds the paginated query.
     * @param baseQuery
     * @param orderBy
     * @param searchParams
     * @return
     */
    public static String buildPaginatedQuery(String baseQuery, String orderBy, SearchParams searchParams) {
        if (isEmpty(searchParams)) {
            if (!"".equals(CommonUtils.NVL(searchParams.getOrderByClause()))) {
                orderBy = searchParams.getOrderByClause();
            }
        }
        String finalQuery = baseQuery + " " + CommonUtils.NVL(orderBy);
        return finalQuery;
    }

    /**
     * Builds the paginated query.
     * @param baseQuery
     * @return
     */
    public static String buildCountQuery(String baseQuery) {
        StringBuilder sb = new StringBuilder("SELECT COUNT(*) FROM (#BASE_QUERY#) FILTERED_ORDERD_RESULTS ");
        String finalQuery = null;
        finalQuery = sb.toString().replaceAll("#BASE_QUERY#", baseQuery);
        return (null == finalQuery) ? baseQuery : finalQuery;
    }

    /**
     * <p>
     * Escapes the characters in a <code>String</code> to be suitable to pass to an
     * SQL query.
     * </p>
     *
     * <p>
     * For example,
     *
     * <pre>
     * statement
     * 		.executeQuery("SELECT * FROM MOVIES WHERE TITLE='" + StringEscapeUtils.escapeSql("McHale's Navy") + "'");
     * </pre>
     * </p>
     *
     * <p>
     * At present, this method only turns single-quotes into doubled single-quotes
     * (<code>"McHale's Navy"</code> => <code>"McHale''s Navy"</code>). It does not
     * handle the cases of percent (%) or underscore (_) for use in LIKE clauses.
     * </p>
     * <p>
     * see http://www.jguru.com/faq/view.jsp?EID=8881
     *
     * @param str the string to escape, may be null
     * @return a new String, escaped for SQL, <code>null</code> if null string input
     */
    public static String escapeSql(String str) {
        if (str == null) {
            return null;
        }
        return str.replace("'", "''").replace("/", "//").replace("_", "/_").replace("%", "/%");
    }

    /**
     * getAvatarPath
     *
     * @param fileId
     * @return
     */
    public static String getAvatarPath(String fileId) {
        if (hasText(fileId)) {
            return String.format("%s/avatar/image/%s", CommonUtils.getConfig("fileStorage.serverUrl"), fileId);
        } else {
            return null;
        }
    }

    /**
     * Loai cac ki tu /, \, null trong ten file Fix loi path traversal
     *
     * @param input : string
     * @return String
     */
    public static String getSafeFileName(String input) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != '/' && c != '\\' && c != 0) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * @param fileName
     * @return
     */
    public static boolean isAllowedType(String fileName) {
        if (hasText(fileName)) {
            String[] allowedType = {".jpg", ".jpeg", ".png", ".doc", ".docx", ".xls", ".xlsx", ".pdf", ".rar", ".zip",
                    ".gif", ".txt", ".log", ".xml", ".7zip"};
            String ext = extractFileExt(fileName);
            if (ext == null) {
                return true;
            }
            ext = ext.toLowerCase();
            for (String extendFile : allowedType) {
                if (extendFile.equals(ext)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param fileName
     * @return
     */
    public static String extractFileExt(String fileName) {
        int dotPos = fileName.lastIndexOf(".");
        if (dotPos != -1) {
            return fileName.substring(dotPos);
        }
        return null;
    }

    /**
     * Luu file len server.
     *
     * @param uploadFile Doi tuong FormFile
     * @param fileName   Ten file
     * @param uploadPath Duong dan thu muc
     * @throws Exception Exception
     */
    public static void saveFile(MultipartFile uploadFile, String fileName, String uploadPath) throws Exception {
        if (isAllowedType(uploadFile.getName())) {
            File desDir = new File(uploadPath);
            if (!desDir.exists()) {
                desDir.mkdir();
            }
            String url = desDir.getAbsolutePath() + File.separator + getSafeFileName(fileName);
            OutputStream outStream = new FileOutputStream(url);
            InputStream inStream = uploadFile.getInputStream();
            int bytesRead;
            byte[] buffer = new byte[1024 * 8];
            while ((bytesRead = inStream.read(buffer, 0, 1024 * 8)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
            inStream.close();
            outStream.close();
        } else {
            throw new Exception("FILE TYPE NOT ALLOW");
        }
    }


    /**
     * So ngay trong thang.
     *
     * @param month Thang, thang chuan
     * @param year  Nam, nam chuan
     * @return So ngay trong thang
     */
    public static int getDaysOfMonth(int month, int year) {
        final int[] MONTH_DAYS = new int[]{0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int num = MONTH_DAYS[month];
        if (month == 2) {
            if ((year % 400 == 0) || (year % 4 == 0 && year % 100 != 0)) {
                num++;
            }
        }
        return num;
    }

    /**
     * random code
     * @param count size of random code
     * @return
     */
    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    /**
     * filterConflictDate
     *
     * @return
     */
    public static void filterConflictDate(Date startDate, Date endDate, StringBuilder queryString, List<Object> paramList
            , String fieldStartDate, String fieldEndDate) {
        String cond = " AND (0=1 ";
        if (endDate == null) { // input endDate == null
            /**
             * ----------------Si|------------------------------->Ei
             * -------------------S|------------------------------->E
             */
            cond += " OR ( " + fieldEndDate + " IS NOT NULL ";
            cond += "     AND DATE(" + fieldEndDate + ") >= ? ) ";
            paramList.add(startDate);
            cond += " OR ( " + fieldEndDate + " IS NULL ";
            cond += "     AND DATE(" + fieldStartDate + ") >= ? ) ";
            paramList.add(startDate);
        } else { // input endDate != null
            /**
             * ----------------Si|-------------------------------|Ei
             * ------------------------S-----------------------E-------->
             */
            cond += " OR ( " + fieldEndDate + " IS NOT NULL ";
            cond += "     AND DATE(" + fieldEndDate + ") >= ? ";
            paramList.add(startDate);
            cond += "     AND DATE(?) >= " + fieldStartDate + " ) ";
            paramList.add(endDate);

            cond += " OR ( " + fieldEndDate + " IS NULL ";
            cond += "     AND DATE(" + fieldStartDate + ") BETWEEN ? AND ? ) ";
            paramList.add(startDate);
            paramList.add(endDate);
        }
        cond += " ) ";
        queryString.append(cond);
    }

    /**
     * ham kiem tra data picker co duoc cau hinh lay du lieu theo thi truong hang khong
     *
     * @param objectBO
     * @return
     */
    public static boolean isByMarketCompany(String objectBO) {
        // TODO Auto-generated method stub
        ResourceBundle rb = ResourceBundle.getBundle("dataPicker");
        try {
            return "true".equalsIgnoreCase(rb.getString(String.format("%s.marketCompanyId", objectBO)));
        } catch (Exception e) {
            log.error("isByMarketCompany:", e);
            return false;
        }
    }

    /**
     * ham kiem tra data picker co duoc cau hinh lay du lieu theo thi truong hang khong
     *
     * @param objectBO
     * @return
     */
    public static boolean isSearchByDomainData(String objectBO) {
        // TODO Auto-generated method stub
        ResourceBundle rb = ResourceBundle.getBundle("dataPicker");
        try {
            return "true".equalsIgnoreCase(rb.getString(String.format("%s.searchByDomainData", objectBO)));
        } catch (Exception e) {
            log.error("isSearchByDomainData:", e);
            return false;
        }
    }

    /**
     * ham kiem tra data picker co duoc cau hinh lay du lieu đa ngôn ngữ hay không
     *
     * @param objectBO
     * @return
     */
    public static boolean isMultiLanguage(String objectBO) {
        // TODO Auto-generated method stub
        ResourceBundle rb = ResourceBundle.getBundle("dataPicker");
        try {
            return "true".equalsIgnoreCase(rb.getString(String.format("%s.isMultiLanguage", objectBO)));
        } catch (Exception e) {
            log.error("isMultiLanguage:", e);
            return false;
        }
    }

    /**
     * getDataPickerConfig
     *
     * @param key
     * @return
     */
    public static String getDataPickerConfig(String key) {
        ResourceBundle rb = ResourceBundle.getBundle("dataPicker");
        return rb.getString(key);
    }

    /**
     * Cau hinh vuot https.
     *
     * @return
     */
    public static TrustManager[] get_trust_mgr() {
        return new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs1, String t) {
            }

            public void checkServerTrusted(X509Certificate[] certs1, String t) {
            }
        }};
    }

    /**
     * By pass link https.
     *
     * @param linkRSS
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     * @throws IOException
     * @throws KeyStoreException
     */
    public static void byPassHttps(URL linkRSS) throws NoSuchAlgorithmException,
            KeyManagementException, IOException, KeyStoreException {
        // Create a context that doesn't check certificates.
        SSLContext ssl_ctx = SSLContext.getInstance("TLS");
        TrustManager[] trust_mgr = get_trust_mgr();
        ssl_ctx.init(null, // key manager
                trust_mgr, // trust manager
                new SecureRandom()); // random number generator
        HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx
                .getSocketFactory());

        HttpsURLConnection con = (HttpsURLConnection) linkRSS.openConnection();

        // Guard against "bad hostname" errors during handshake.
        con.setHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String host, SSLSession sess) {
                return "localhost".equals(host);
            }
        });
    }

    public static Double getDoubleValue(Object value) {
        if (value != null && !value.toString().isEmpty()) {
            return Double.valueOf(value.toString());
        } else {
            return 0d;
        }
    }

    public static Long getLongValue(Object value) {
        if (value != null && !value.toString().isEmpty()) {
            return (Double.valueOf(value.toString())).longValue();
        } else {
            return 0L;
        }
    }

    public static BigDecimal convertEtoDecimal(Double value) {
        return new BigDecimal(value);
    }

    /**
     * Chuyen xau thanh so nguyen. Kiem tra luon neu xau khong hop le se tra ve
     * null.
     *
     * @param s Xau
     * @return So
     */
    public static Double parseDouble(String s) {
        if (hasText(s)) {
            try {
                return Double.parseDouble(s);
            } catch (Exception ex) {
                return null;
            }
        } else {
            return null;
        }
    }

    public static Double parseDouble(String s, Double defaultValue) {
        if (hasText(s)) {
            try {
                return Double.parseDouble(s);
            } catch (Exception ex) {
                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }

    /**
     * Convert mot so sang dang so La Ma.
     *
     * @param decimal So binh thuong
     * @return So La Ma
     */
    public static String convertDecimalToRoman(int decimal) {
        final String[] ROMAN_CODE = {"M", "CM", "D", "CD", "C", "XC", "L",
                "XL", "X", "IX", "V", "IV", "I"};
        final int[] DECIMAL_VALUE = {1000, 900, 500, 400, 100, 90, 50,
                40, 10, 9, 5, 4, 1};
        if (decimal <= 0 || decimal >= 4000) {
            throw new NumberFormatException("Value outside roman numeral range.");
        }
        StringBuilder roman = new StringBuilder();
        for (int i = 0; i < ROMAN_CODE.length; i++) {
            while (decimal >= DECIMAL_VALUE[i]) {
                decimal -= DECIMAL_VALUE[i];
                roman.append(ROMAN_CODE[i]);
            }
        }
        return roman.toString();
    }

    /**
     * Sắp xếp thời gian theo thứ tự tăng dần
     *
     * @param listOriginal
     */
    public static void sortListDateASC(List<LocalDate> listOriginal) {
        listOriginal.sort(new Comparator<LocalDate>() {
            @Override
            public int compare(LocalDate arg0, LocalDate arg1) {
                if (arg0.isBefore(arg1)) {
                    return -1;
                } else {
                    if (arg0.equals(arg1)) {
                        return 0;
                    } else {
                        return 1;
                    }
                }
            }
        });
    }

    /**
     * convertNumberColumnToColumnLabel
     * chuyển cột số sang tên cột trong file import
     *
     * @param colData
     * @return
     */
    public static String convertNumberColumnToColumnLabel(int colData) {
        String columnLabel = "";
        if (colData < 26) {
            columnLabel = String.valueOf((char) ('A' + colData));
        } else {
            int temp = colData / 26;
            colData -= 26 * temp;
            String result = String.valueOf((char) ('A' + temp - 1));
            columnLabel = result + (char) ('A' + colData);
        }
        return columnLabel;
    }

    /**
     * Format tiền
     *
     * @param d So
     * @return Xau
     */
    public static String formatNumber2Currency(Double d) {
        if (d == null) {
            return "";
        } else {
            DecimalFormat format = new DecimalFormat("###,###.###");
            return format.format(d);
        }
    }

    /**
     * Format tiền
     *
     * @param d So
     * @return Xau
     */
    public static String formatNumber2Currency(Long d) {
        if (d == null) {
            return "";
        } else {
            DecimalFormat format = new DecimalFormat("###,###,###");
            return format.format(d);
        }
    }

    /**
     * filter
     *
     * @param strCondition
     * @param paramList
     * @param listId
     */
    public static void filter(List<Long> listId, StringBuilder strCondition, List<Object> paramList, String field) {
        boolean first = true;
        if (!CommonUtils.isNullOrEmpty(listId)) {
            strCondition.append(" AND ( ");
            for (Long id : listId) {
                if (!first) {
                    strCondition.append(" OR ");
                } else {
                    first = false;
                }
                strCondition.append(" CONCAT(','," + field + ",',') LIKE ? ");
                paramList.add("%," + id.toString() + ",%");
            }

            strCondition.append(" ) ");
        }
    }


    /**
     * Returns this host's non-loopback IPv4 addresses.
     *
     * @return
     * @throws SocketException
     */
    public static String getInet4Addresses() throws SocketException {
        List<Inet4Address> ret = new ArrayList<Inet4Address>();

        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface netint : Collections.list(nets)) {
            Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
            for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                if (inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress()) {
                    ret.add((Inet4Address) inetAddress);
                }
            }
        }

        return !ret.isEmpty()
                ? ret.get(0).getHostAddress()
                : null;
    }

    /**
     * Lấy requestID
     * @return String
     */
    public static String getRequestId() {
        return MDC.get("RequestId");
    }
}
