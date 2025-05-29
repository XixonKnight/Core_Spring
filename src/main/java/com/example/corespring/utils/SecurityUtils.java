package com.example.corespring.utils;


import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@UtilityClass
public class SecurityUtils {
    /**
     * lay user id login
     *
     * @return
     */
    public static String getUserLoginName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                return ((UserDetails) principal).getUsername();
            } else {
                return principal.toString(); // fallback
            }
        }

        return null;
    }

    /**
     * getCurrentLanguage
     *
     * @return
     */
    public static String getCurrentLanguage(HttpServletRequest req) {
        // TODO Auto-generated method stub
        String langCode = req.getHeader("Current-Language");
        return CommonUtils.NVL(langCode, "vi");
    }

}
