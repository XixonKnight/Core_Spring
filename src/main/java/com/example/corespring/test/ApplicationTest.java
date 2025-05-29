package com.example.corespring.test;


import com.example.corespring.offices.excels.ExcelReadConfig;
import com.example.corespring.offices.excels.PostalOffice;
import com.example.corespring.customs.CustomMultipartFile;
import com.example.corespring.utils.BundleUtils;
import com.example.corespring.utils.CommonUtils;
import com.example.corespring.offices.excels.ColumnConfig;
import com.example.corespring.core.response.Response;
import com.example.corespring.core.enums.ErrorCodes;
import com.example.corespring.offices.excels.ExcelUtils;
import com.example.corespring.exceptions.ApplicationException;
import com.example.corespring.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

//    @PostMapping("/excel/import")
//    public Response importExcel(@ModelAttribute RequestCommon request) {
//        try {
//            // Khởi tạo workbook tùy theo loại file
//            List<ColumnConfig> result = ExcelUtils.loadColumnRequirementsFromCfg(request.getPath());
//            Workbook workbook = ExcelUtils.createWorkbook(request.getFile());
//            List<String> header = ExcelUtils.getHeadersMergeFromExcel(workbook, request.getSheetIndex(), request.getCountRowHeader());
//            List<String> validate= ExcelUtils.validateExcelHeaders(header,result);
//            return Response.success().withData(CommonUtils.objectToJson(validate));
//        } catch (ApplicationException e) {
//            throw e;
//        } catch (Exception e) {
//            log.error("ApplicationTest | importExcel | Exception : {}", e.getMessage(), e);
//            throw new ApplicationException(ErrorCodes.SYSTEM_ERROR);
//        }
//    }


    public static void main(String[] args) throws IOException {
        String pathConfig = "config/template_not_merge_header.yaml";
        String file = "D:\\VNPOST\\data_mau\\Template_Import_BuuCuc.xlsx";
        List<PostalOffice> res = importExcel(pathConfig, file, PostalOffice.class, 3);
        log.info("res: {}", JsonUtils.toJson(res));
//        String pathConfig = "config/template_merge_header.yaml";
//        String file = "D:\\VNPOST\\data_mau\\1_Template_Import_Phan_Ky_Theo_Quan_Huyen_Thi_Xa.xlsx";
//        List<PostalMonthlyData> data= importExcel(pathConfig, file, PostalMonthlyData.class, 3);
//        ServiceTest serviceTest = new ServiceTest();
//        List<PostalData> res = serviceTest.convertListPostData(data);
//        log.info("res: {}", JsonUtils.toJson(res.get(0)));
    }

    private static <T> List<T> importExcel(String pathConfig, String pathFile, Class<T> clazz, int headerRowCount) throws IOException {
        List<ColumnConfig> result = ExcelUtils.loadColumnRequirementsFromCfg(pathConfig);

        File file = new File(pathFile);
        MultipartFile multipartFile = new CustomMultipartFile(file, "application/xlsx");

        Workbook workbook = ExcelUtils.createWorkbook(multipartFile);
        List<String> header = ExcelUtils.getHeaderExcel(workbook, 0, headerRowCount, false);
        List<String> validate= ExcelUtils.validateExcelHeaders(header,result);
        if (!validate.isEmpty()) {
            log.info("validate : {}", JsonUtils.toJson(validate));
            return new ArrayList<>();
        }

        ExcelReadConfig<T> excelReadConfig = ExcelReadConfig.<T>builder()
                .workbook(workbook)
                .sheetNumber(0)
                .rowStartRead(headerRowCount)
                .headerConfig(result)
                .excelHeaders(header)
                .targetClass(clazz)
                .build();

        return ExcelUtils.readDataExcel(excelReadConfig);
    }


}
