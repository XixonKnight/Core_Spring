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
public class ServiceMonth {
    String month;
    BigDecimal BCCP;
    BigDecimal TCBC;
    BigDecimal KDPP;
    BigDecimal HCC;
}
