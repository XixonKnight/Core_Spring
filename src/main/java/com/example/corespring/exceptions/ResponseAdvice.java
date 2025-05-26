package com.example.corespring.exceptions;


import com.example.corespring.common.CommonUtils;
import com.example.corespring.domain.response.Response;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, @NotNull Class<? extends HttpMessageConverter<?>> converterType) {
        // Kiểm tra nếu returnType là kiểu Response, ta sẽ thêm requestId vào
        return Response.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public Object beforeBodyWrite(Object body, @NotNull MethodParameter returnType,
                                  @NotNull MediaType selectedContentType, @NotNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @NotNull ServerHttpRequest request, @NotNull ServerHttpResponse response) {

        // Nếu body là null, không cần phải làm gì
        if (body == null) {
            return null;
        }

        // Kiểm tra nếu body là đối tượng Response và gán requestId vào đó
        if (body instanceof Response responseObj) {
            responseObj.setRequestId(CommonUtils.getRequestId()); // Gắn requestId vào response
        }

        return body;
    }

}
