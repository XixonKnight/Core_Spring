package com.example.corespring.common;

import com.example.corespring.core.base.DataTableResults;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

import java.util.List;

public interface VfData {
    public <T> DataTableResults<T>
    findPaginationQuery(String nativeQuery, String nativeQueryCount,
                        String orderBy, List<Object> paramList, Class<T> obj);

    /**
     *
     * @param nativeQuery
     * @param orderBy
     * @param paramList
     * @param obj
     * @return
     * @param <T>
     */
    public <T> DataTableResults<T> findPaginationQuery(String nativeQuery, String orderBy, List<Object> paramList, Class<T> obj);

    /**
     *
     * @param nativeQuery
     * @param orderBy
     * @param paramList
     * @param obj
     * @return
     * @param <T>
     */
    public <T> List<T> findAllData(String nativeQuery, String orderBy, List<Object> paramList, Class obj);


    /**
     * Get session.
     *
     */
    public Session getSession();

    public NativeQuery createNativeQuery(String sql);
    /**
     * ham set result transformer cua cau query
     *
     * @param query
     *            cau query
     *            doi tuong
     */
    public void setResultTransformer(NativeQuery<?> query, Class<?> targetClass);

    /**
     * Get list alias column.
     *
     * @param query
     * @return
     */
    public List<String> getReturnAliasColumns(NativeQuery query);
}
