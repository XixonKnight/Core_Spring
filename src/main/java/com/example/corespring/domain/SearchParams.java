package com.example.corespring.domain;

import com.example.corespring.common.CommonUtils;
import lombok.Getter;
import lombok.Setter;

//{"first":0,"rows":2,"sortField":"isUsed","sortOrder":1,"filters":{},"globalFilter":null}
@Setter
@Getter
public class SearchParams {
    /**
     * -- GETTER --
     *
     *
     * -- SETTER --
     *
     @return the first
      * @param first the first to set
     */
    private Integer first;
    /**
     * -- GETTER --
     *
     *
     * -- SETTER --
     *
     @return the rows
      * @param rows the rows to set
     */
    private Integer rows;
    /**
     * -- GETTER --
     *
     *
     * -- SETTER --
     *
     @return the sortField
      * @param sortField the sortField to set
     */
    private String sortField;
    /**
     * -- GETTER --
     *
     *
     * -- SETTER --
     *
     @return the sortOrder
      * @param sortOrder the sortOrder to set
     */
    private Integer sortOrder;
    /**
     * -- GETTER --
     *
     *
     * -- SETTER --
     *
     @return the globalFilter
      * @param globalFilter the globalFilter to set
     */
    private String globalFilter;

    /**
     * Gets the order by clause.
     *
     * @return the order by clause
     */
    public String getOrderByClause() {

        StringBuilder sbsb = null;

        if (!CommonUtils.hasText(this.sortField)) {
            String sortString = (1 == this.sortOrder) ? SORT_ASC : SORT_DESC;
            sbsb = new StringBuilder();
            sbsb.append(ORDER_BY).append(this.sortField).append(SPACE).append(sortString);
        }

        return (null == sbsb) ? BLANK : sbsb.toString();
    }

    /**
     * The Constant ORDER_BY.
     */
    private static final String ORDER_BY = " ORDER BY ";
    /**
     * The Constant SPACE.
     */
    private static final String SPACE = " ";
    /**
     * The Constant SORT.
     */
    private static final String SORT_ASC = "ASC";
    private static final String SORT_DESC = "DESC";
    /**
     * The Constant BLANK.
     */
    private static final String BLANK = "";
}