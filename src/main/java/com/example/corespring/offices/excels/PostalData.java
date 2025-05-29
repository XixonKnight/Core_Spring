package com.example.corespring.offices.excels;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostalData {
    String provinceCode;
    String provinceName;
    String districtCode;
    String districtName;
    List<ServiceMonth> serviceMonths;
}
