package com.example.corespring.offices.excels;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostalOffice {
    @KeyReadExcel("PROVINCE_CODE")
    String provinceCode;

    @KeyReadExcel("PROVINCE_NAME")
    String provinceName;

    @KeyReadExcel("DISTRICT_CODE")
    String districtCode;

    @KeyReadExcel("DISTRICT_NAME")
    String districtName;

    @KeyReadExcel("POSTAL_CODE")
    String postalCode;

    @KeyReadExcel("POSTAL_NAME")
    String postalName;

    @KeyReadExcel("LEVEL")
    String level;

    @KeyReadExcel("TYPE")
    String type;

    @KeyReadExcel("STATUS")
    String status;

    @KeyReadExcel("EMPLOYEES")
    String emp_count;
}
