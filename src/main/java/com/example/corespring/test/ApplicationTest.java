package com.example.corespring.test;


import com.example.corespring.common.BundleUtils;
import com.example.corespring.common.CommonUtils;
import com.example.corespring.domain.response.Response;
import com.example.corespring.enums.ErrorCodes;
import com.example.corespring.exceptions.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/test")
public class ApplicationTest {
    @RequestMapping("/exception/system")
    public Response systemException() {
        log.info("ApplicationTest | hello");
        throw new ApplicationException(ErrorCodes.SYSTEM_ERROR);
    }
    @RequestMapping("/messages")
    public Response getAllMessages() {
        return Response.success().withData(CommonUtils.objectToJson(BundleUtils.getAllMessages()));
    }

}
