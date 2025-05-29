package com.example.corespring.offices.excels;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostalMonthlyData {

    @KeyReadExcel("PROVINCE_CODE")
    String provinceCode;
    @KeyReadExcel("PROVINCE_NAME")
    String provinceName;

    @KeyReadExcel("DISTRICT_CODE")
    String districtCode;
    @KeyReadExcel("DISTRICT_NAME")
    String districtName;

    // Tháng 1
    @KeyReadExcel("M1_BCCP")
    BigDecimal M1_BCCP;
    @KeyReadExcel("M1_TCBC")
    BigDecimal M1_TCBC;
    @KeyReadExcel("M1_KDPP")
    BigDecimal M1_KDPP;
    @KeyReadExcel("M1_HCC")
    BigDecimal M1_HCC;

    // Tháng 2
    @KeyReadExcel("M2_BCCP")
    BigDecimal M2_BCCP;
    @KeyReadExcel("M2_TCBC")
    BigDecimal M2_TCBC;
    @KeyReadExcel("M2_KDPP")
    BigDecimal M2_KDPP;
    @KeyReadExcel("M2_HCC")
    BigDecimal M2_HCC;

    // Tháng 3
    @KeyReadExcel("M3_BCCP")
    BigDecimal M3_BCCP;
    @KeyReadExcel("M3_TCBC")
    BigDecimal M3_TCBC;
    @KeyReadExcel("M3_KDPP")
    BigDecimal M3_KDPP;
    @KeyReadExcel("M3_HCC")
    BigDecimal M3_HCC;


    // Tháng 4
    @KeyReadExcel("M4_BCCP")
    BigDecimal M4_BCCP;
    @KeyReadExcel("M4_TCBC")
    BigDecimal M4_TCBC;
    @KeyReadExcel("M4_KDPP")
    BigDecimal M4_KDPP;
    @KeyReadExcel("M4_HCC")
    BigDecimal M4_HCC;

    //Tháng 5
    @KeyReadExcel("M5_BCCP")
    BigDecimal M5_BCCP;
    @KeyReadExcel("M5_TCBC")
    BigDecimal M5_TCBC;
    @KeyReadExcel("M5_KDPP")
    BigDecimal M5_KDPP;
    @KeyReadExcel("M5_HCC")
    BigDecimal M5_HCC;

    //Tháng 6
    @KeyReadExcel("M6_BCCP")
    BigDecimal M6_BCCP;
    @KeyReadExcel("M6_TCBC")
    BigDecimal M6_TCBC;
    @KeyReadExcel("M6_KDPP")
    BigDecimal M6_KDPP;
    @KeyReadExcel("M6_HCC")
    BigDecimal M6_HCC;

    //Tháng 7
    @KeyReadExcel("M7_BCCP")
    BigDecimal M7_BCCP;
    @KeyReadExcel("M7_TCBC")
    BigDecimal M7_TCBC;
    @KeyReadExcel("M7_KDPP")
    BigDecimal M7_KDPP;
    @KeyReadExcel("M7_HCC")
    BigDecimal M7_HCC;

    //Tháng 8
    @KeyReadExcel("M8_BCCP")
    BigDecimal M8_BCCP;
    @KeyReadExcel("M8_TCBC")
    BigDecimal M8_TCBC;
    @KeyReadExcel("M8_KDPP")
    BigDecimal M8_KDPP;
    @KeyReadExcel("M8_HCC")
    BigDecimal M8_HCC;

    //Tháng 9
    @KeyReadExcel("M9_BCCP")
    BigDecimal M9_BCCP;
    @KeyReadExcel("M9_TCBC")
    BigDecimal M9_TCBC;
    @KeyReadExcel("M9_KDPP")
    BigDecimal M9_KDPP;
    @KeyReadExcel("M9_HCC")
    BigDecimal M9_HCC;

    //Tháng 10
    @KeyReadExcel("M10_BCCP")
    BigDecimal M10_BCCP;
    @KeyReadExcel("M10_TCBC")
    BigDecimal M10_TCBC;
    @KeyReadExcel("M10_KDPP")
    BigDecimal M10_KDPP;
    @KeyReadExcel("M10_HCC")
    BigDecimal M10_HCC;

    //Tháng 11
    @KeyReadExcel("M11_BCCP")
    BigDecimal M11_BCCP;
    @KeyReadExcel("M11_TCBC")
    BigDecimal M11_TCBC;
    @KeyReadExcel("M11_KDPP")
    BigDecimal M11_KDPP;
    @KeyReadExcel("M11_HCC")
    BigDecimal M11_HCC;

    //Tháng 12
    @KeyReadExcel("M12_BCCP")
    BigDecimal M12_BCCP;
    @KeyReadExcel("M12_TCBC")
    BigDecimal M12_TCBC;
    @KeyReadExcel("M12_KDPP")
    BigDecimal M12_KDPP;
    @KeyReadExcel("M12_HCC")
    BigDecimal M12_HCC;

}
