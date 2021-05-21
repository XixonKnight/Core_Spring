package com.example.corespring.common;

import domain.PaginationResult;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import java.util.List;

/**
 * Created by Nhan Nguyen on 5/21/2021
 *
 * @author Nhan Nguyen
 * @date 5/21/2021
 */
public interface VfPagination {

    public Session getSession();

    public SQLQuery createSQLQuery(String sql);
    /**
     * ham set result transformer cua cau query
     *
     * @param query
     *            cau query
     * @param obj
     *            doi tuong
     */
    public void setResultTransformer(SQLQuery query, Class obj);

    /**
     * Get list alias column.
     *
     * @param query
     * @return
     */
    public List<String> getReturnAliasColumns(SQLQuery query);

    public <T> PaginationResult<T> findPaginationQuery(String nativeQuery, String orderBy,
                                                       List<Object> paramList, Class obj);

}
