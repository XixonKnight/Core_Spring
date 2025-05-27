package com.example.corespring.test;


import com.example.corespring.common.BundleUtils;
import com.example.corespring.common.CommonUtils;
import com.example.corespring.core.base.ColumnConfig;
import com.example.corespring.core.request.RequestCommon;
import com.example.corespring.core.response.Response;
import com.example.corespring.core.enums.ErrorCodes;
import com.example.corespring.document.excel.ExcelUtils;
import com.example.corespring.exceptions.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/test")
public class ApplicationTest {
    @GetMapping("/exception/system")
    public Response systemException() {
        log.info("ApplicationTest | hello");
        throw new ApplicationException(ErrorCodes.SYSTEM_ERROR);
    }

    @GetMapping("/messages")
    public Response getAllMessages() {
        return Response.success().withData(CommonUtils.objectToJson(BundleUtils.getAllMessages()));
    }

    @GetMapping("/excel/config")
    public Response templateConfigExcel(@RequestParam(required = false) String fileName) {
        List<ColumnConfig> result = ExcelUtils.loadColumnRequirementsFromCfg(fileName);
        return Response.success().withData(CommonUtils.objectToJson(result));
    }

    @PostMapping("/excel/import")
    public Response importExcel(@ModelAttribute RequestCommon request) {
        try {
            // Khởi tạo workbook tùy theo loại file
            List<ColumnConfig> result = ExcelUtils.loadColumnRequirementsFromCfg(request.getPath());
            Workbook workbook = ExcelUtils.createWorkbook(request.getFile());
            List<String> header = ExcelUtils.getHeadersMergeFromExcel(workbook, request.getSheetIndex(), request.getCountRowHeader());
            List<String> validate= ExcelUtils.validateExcelHeaders(header,result);
            return Response.success().withData(CommonUtils.objectToJson(validate));
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            log.error("ApplicationTest | importExcel | Exception : {}", e.getMessage(), e);
            throw new ApplicationException(ErrorCodes.SYSTEM_ERROR);
        }
    }


}
