package com.example.corespring.offices.excels;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExcelReadConfig<T> {
     Workbook workbook;
     List<ColumnConfig> headerConfig;
     List<String> excelHeaders;
     int sheetNumber;
     int rowStartRead;
     Class<T> targetClass;
}
