package com.example.corespring.test;


import com.example.corespring.offices.excels.PostalData;
import com.example.corespring.offices.excels.ServiceMonth;
import com.example.corespring.offices.excels.PostalMonthlyData;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceTest {
    public List<PostalData> convertListPostData(List<PostalMonthlyData> list) {
        try {
            List<PostalData> res = new ArrayList<>();
            for (PostalMonthlyData item : list) {
                PostalData data = new PostalData();
                List<ServiceMonth> serviceMonths = new ArrayList<>();
                data.setProvinceCode(item.getProvinceCode());
                data.setProvinceName(item.getProvinceName());
                data.setDistrictCode(item.getDistrictCode());
                data.setDistrictName(item.getDistrictName());

                for (int month = 1; month <= 12; month++) {
                    String prefix = "getM" + month + "_";

                    ServiceMonth sm = ServiceMonth.builder()
                            .month(String.valueOf(month))
                            .BCCP((BigDecimal) PostalMonthlyData.class.getMethod(prefix + "BCCP").invoke(item))
                            .TCBC((BigDecimal) PostalMonthlyData.class.getMethod(prefix + "TCBC").invoke(item))
                            .KDPP((BigDecimal) PostalMonthlyData.class.getMethod(prefix + "KDPP").invoke(item))
                            .HCC((BigDecimal) PostalMonthlyData.class.getMethod(prefix + "HCC").invoke(item))
                            .build();

                    serviceMonths.add(sm);
                }

                data.setServiceMonths(serviceMonths);
                res.add(data);
            }
            return res;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
