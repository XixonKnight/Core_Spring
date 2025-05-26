package com.example.corespring.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Getter
public class DataTableResults<T> {
    /**
     * The draw.
     * -- GETTER --
     *  Gets the draw.
     *
     *
     * -- SETTER --
     *  Sets the draw.
     *
     @return the draw
      * @param draw the draw to set

     */
    private String draw;
    /**
     * -- GETTER --
     *
     *
     * -- SETTER --
     *
     @return the first
      * @param first the first to set
     */
    private String first;

    /**
     * The records filtered.
     * -- GETTER --
     *  Gets the records filtered.
     *
     *
     * -- SETTER --
     *  Sets the records filtered.
     *
     @return the recordsFiltered
      * @param recordsFiltered the recordsFiltered to set

     */
    private String recordsFiltered;

    /**
     * The records total.
     * -- GETTER --
     *  Gets the records total.
     *
     *
     * -- SETTER --
     *  Sets the records total.
     *
     @return the recordsTotal
      * @param recordsTotal the recordsTotal to set

     */
    private String recordsTotal;

    /**
     * The list of data objects.
     * -- GETTER --
     *  Get list data
     *
     *
     * -- SETTER --
     *  Set list data
     *
     @return
      * @param data

     */
    List<T> data;

    /**
     * Header config
     * -- GETTER --
     *
     *
     * -- SETTER --
     *
     @return the headerConfig
      * @param headerConfig the headerConfig to set

     */
    List<T> headerConfig;
    private Map<String, Object> extendData = new HashMap<>();


}
