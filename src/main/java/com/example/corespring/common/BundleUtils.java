package com.example.corespring.common;


import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

@UtilityClass
@Slf4j
public class BundleUtils {
    private static final String BUNDLE_NAME = "messages"; // tên file messages.properties (không có .properties)

    /**
     * getMailConfigProperties
     *
     * @param key
     * @return
     */
    public static String getMailConfigProperties(String key) {
        ResourceBundle rb = ResourceBundle.getBundle("mail");
        return rb.getString(key);
    }
    /**
     * Lay xau gia tri tu file ApplicationResources.properties.
     *
     * @param key Khoa
     * @return Gia tri
     */
    public static String getApplicationResource(HttpServletRequest req, String key) {
        try {
            String locale = SecurityUtils.getCurrentLanguage(req);
            ResourceBundle rb = ResourceBundle.getBundle("ApplicationResources", new Locale(locale));
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
    public static String getApplicationResource(HttpServletRequest req, String key, Object... args) {
        try {
            String locale = SecurityUtils.getCurrentLanguage(req);
            ResourceBundle rb = ResourceBundle.getBundle("ApplicationResources", new Locale(locale));
            return String.format(rb.getString(key), args);
        } catch (Exception ex) {
            log.error("getApplicationResource:", ex);
            return "";
        }

    }

    /**
     * prefixByLanguage
     *
     * @param req
     * @return
     */
    public static String prefixByLanguage(HttpServletRequest req) {
        String lang = SecurityUtils.getCurrentLanguage(req);
        if ("vi".equals(lang)) {
            return "vi";
        } else {
            return "en";
        }
    }

    public static Map<String, String> getAllMessages() {
        Locale currentLocale = LocaleContextHolder.getLocale();
        ResourceBundle resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME, currentLocale);
        Map<String, String> map = new LinkedHashMap<>();
        if (resourceBundle != null) {
            Enumeration<String> keys = resourceBundle.getKeys();
            while (keys.hasMoreElements()) {
                String key = keys.nextElement();
                map.put(key, resourceBundle.getString(key));
            }
        }
        return map;
    }

}
