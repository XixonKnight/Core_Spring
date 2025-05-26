package com.example.corespring.common;

import com.example.corespring.domain.DataTableResults;
import com.example.corespring.domain.SearchParams;
import com.google.gson.Gson;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class VfDataIml implements VfData {

    @PersistenceContext
    final EntityManager entityManager;

    final HttpServletRequest req;

    @Override
    public Session getSession() {
        return entityManager.unwrap(Session.class);
    }


    @Override
    @SuppressWarnings("deprecation")
    public NativeQuery createNativeQuery(String sql) {
        return getSession().createNativeQuery(sql);
    }

    @Override
    public void setResultTransformer(NativeQuery<?> query, Class<?> targetClass) {
        Map<String, String> fieldTypeMap = Arrays.stream(targetClass.getDeclaredFields())
                .collect(Collectors.toMap(Field::getName, field -> field.getGenericType().toString()));

        List<String> aliasColumns = getReturnAliasColumns(query);

        for (String alias : aliasColumns) {
            String type = fieldTypeMap.get(alias);

            if (type == null) {
                log.debug("{} is not defined in {}", alias, targetClass.getSimpleName());
                continue;
            }

            Class<?> scalarType = resolveJavaType(type);

            if (scalarType == null) {
                log.debug("{} is not supported", type);
                continue;
            }

            query.addScalar(alias, scalarType);
        }

        query.setResultTransformer(Transformers.aliasToBean(targetClass));
    }

    private Class<?> resolveJavaType(String typeName) {
        return switch (typeName) {
            case "class java.lang.Long"          -> Long.class;
            case "class java.lang.Integer"       -> Integer.class;
            case "class java.lang.Double"        -> Double.class;
            case "class java.lang.String"        -> String.class;
            case "class java.lang.Boolean"       -> Boolean.class;
            case "class java.util.Date",
                 "class java.time.LocalDateTime" -> LocalDateTime.class;
            default                              -> null;
        };
    }




    @Override
    public List<String> getReturnAliasColumns(NativeQuery query) {
        List<String> aliasColumns = new ArrayList<>();
        String NativeQuery = query.getQueryString();
        NativeQuery = NativeQuery.replace("\n", " ");
        NativeQuery = NativeQuery.replace("\t", " ");
        int numOfRightPythis = 0;
        int startPythis = -1;
        int endPythis = 0;
        boolean hasRightPythis = true;
        while (hasRightPythis) {
            char[] arrStr = NativeQuery.toCharArray();
            hasRightPythis = false;
            int idx = 0;
            for (char c : arrStr) {
                if (idx > startPythis) {
                    if ("(".equalsIgnoreCase(String.valueOf(c))) {
                        if (numOfRightPythis == 0) {
                            startPythis = idx;
                        }
                        numOfRightPythis++;
                    } else if (")".equalsIgnoreCase(String.valueOf(c))) {
                        if (numOfRightPythis > 0) {
                            numOfRightPythis--;
                            if (numOfRightPythis == 0) {
                                endPythis = idx;
                                break;
                            }
                        }
                    }
                }
                idx++;
            }
            if (endPythis > 0) {
                NativeQuery = NativeQuery.substring(0, startPythis) + " # " + NativeQuery.substring(endPythis + 1);
                hasRightPythis = true;
                endPythis = 0;
            }
        }
        String arrStr[] = NativeQuery.substring(0, NativeQuery.toUpperCase().indexOf(" FROM ")).split(",");
        for (String str : arrStr) {
            String[] temp = str.trim().split(" ");
            String alias = temp[temp.length - 1].trim();
            if (alias.contains(".")) {
                alias = alias.substring(alias.lastIndexOf(".") + 1).trim();
            }
            if (alias.contains(",")) {
                alias = alias.substring(alias.lastIndexOf(",") + 1).trim();
            }
            if (alias.contains("`")) {
                alias = alias.replace("`", "");
            }
            if (!aliasColumns.contains(alias)) {
                aliasColumns.add(alias);
            }
        }
        return aliasColumns;
    }


    @Override
    public <T> DataTableResults<T> findPaginationQuery(String nativeQuery, String nativeQueryCount, String orderBy, List<Object> paramList, Class<T> obj) {
        return null;
    }

    @Override
    public <T> DataTableResults<T> findPaginationQuery(String nativeQuery, String orderBy,
                                                       List<Object> paramList, Class<T> obj) {
        return findPagination(nativeQuery, orderBy, paramList, obj, 10);
    }

    @Override
    public <T> List<T> findAllData(String nativeQuery, String orderBy, List<Object> paramList, Class obj) {
        return findALl(nativeQuery, orderBy, paramList, obj);
    }

    private <T> DataTableResults<T> findPagination(String nativeQuery,
                                                   String orderBy,
                                                   List<Object> paramList,
                                                   Class<T> resultClass,
                                                   int defaultLimit) {
        log.info("[VF DATA IMPL] findPagination");

        // Lấy thông tin tìm kiếm từ request
        String searchJson = req.getParameter("_search");
        SearchParams searchParams = CommonUtils.hasText(searchJson)
                ? new Gson().fromJson(searchJson, SearchParams.class)
                : new SearchParams();

        // Xây dựng câu truy vấn phân trang và câu truy vấn đếm tổng số dòng
        String paginatedSql = CommonUtils.buildPaginatedQuery(nativeQuery, orderBy, searchParams);
        String countSql = CommonUtils.buildCountQuery(nativeQuery);

        // Tạo NativeQuery
        NativeQuery query = createNativeQuery(paginatedSql);
        setResultTransformer(query, resultClass);
        query.setFirstResult(CommonUtils.NVL(searchParams.getFirst()));
        query.setMaxResults(CommonUtils.NVL(searchParams.getRows(), defaultLimit));

        NativeQuery<?> countQuery = createNativeQuery(countSql);

        // Gán parameter cho cả query và countQuery nếu có
        if (!CommonUtils.isNullOrEmpty(paramList)) {
            for (int i = 0; i < paramList.size(); i++) {
                Object value = paramList.get(i);
                query.setParameter(i + 1, value);
                countQuery.setParameter(i + 1, value);
            }
        }

        // Thực thi truy vấn
        List<T> dataList = query.getResultList();
        Object totalRecords = countQuery.uniqueResult();

        // Gán dữ liệu vào DataTableResults
        DataTableResults<T> result = new DataTableResults<>();
        result.setData(dataList);

        boolean hasData = !CommonUtils.isEmpty(dataList);
        result.setRecordsTotal(hasData ? String.valueOf(totalRecords) : "0");
        result.setRecordsFiltered(result.getRecordsTotal());
        result.setFirst(String.valueOf(CommonUtils.NVL(searchParams.getFirst())));

        return result;
    }


    private <T> List<T> findALl(String nativeQuery, String orderBy,
                                                   List<Object> paramList, Class obj) {
        log.info("[VF DATA IMPL] findALl");
        String paginatedQuery = CommonUtils.buildPaginatedQuery(nativeQuery, orderBy, null);
        String countStrQuery = CommonUtils.buildCountQuery(nativeQuery);
        NativeQuery query = createNativeQuery(paginatedQuery);
        setResultTransformer(query, obj);
        // pagination
        NativeQuery countQuery = createNativeQuery(countStrQuery);
        if (!CommonUtils.isNullOrEmpty(paramList)) {
            int paramSize = paramList.size();
            for (int i = 0; i < paramSize; i++) {
                countQuery.setParameter(i + 1, paramList.get(i));
                query.setParameter(i + 1, paramList.get(i));
            }
        }
        @SuppressWarnings("unchecked")
        List<T> userList = query.list();

        return userList;
    }

}
