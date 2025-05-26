package com.example.corespring.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class RequestIdFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Tạo một requestId mới cho mỗi request
        String requestId = UUID.randomUUID().toString();

        // Đặt requestId vào MDC để có thể log sau này nếu cần
        MDC.put("RequestId", requestId);

        // Thêm requestId vào header của response
        response.setHeader("X-Request-Id", requestId);

        try {
            // Tiến hành xử lý request
            filterChain.doFilter(request, response);
        } finally {
            // Loại bỏ requestId khỏi MDC sau khi xử lý xong request
            MDC.remove("RequestId");
        }
    }
}

