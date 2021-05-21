package domain;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Nhan Nguyen on 5/19/2021
 *
 * @author Nhan Nguyen
 * @date 5/19/2021
 */
public class DataTableResults<T> {
    /**
     * The draw.
     */
    private String draw;
    private String first;

    /**
     * The records filtered.
     */
    private String recordsFiltered;

    /**
     * The records total.
     */
    private String recordsTotal;

    /**
     * The list of data objects.
     */
    List<T> data;

    /**
     * Header config
     */
    List<T> headerConfig;
    private Map<String, Object> extendData = new HashMap<>();

    public Map<String, Object> getExtendData() {
        return extendData;
    }

    public void setExtendData(Map<String, Object> extendData) {
        this.extendData = extendData;
    }

    public void addExtendData(String key, Object data) {
        extendData.put(key, data);
    }

    public String getJson() {
        return new Gson().toJson(this);
    }

    /**
     * Gets the draw.
     *
     * @return the draw
     */
    public String getDraw() {
        return draw;
    }

    /**
     * Sets the draw.
     *
     * @param draw the draw to set
     */
    public void setDraw(String draw) {
        this.draw = draw;
    }

    /**
     * Gets the records filtered.
     *
     * @return the recordsFiltered
     */
    public String getRecordsFiltered() {
        return recordsFiltered;
    }

    /**
     * Sets the records filtered.
     *
     * @param recordsFiltered the recordsFiltered to set
     */
    public void setRecordsFiltered(String recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    /**
     * Gets the records total.
     *
     * @return the recordsTotal
     */
    public String getRecordsTotal() {
        return recordsTotal;
    }

    /**
     * Sets the records total.
     *
     * @param recordsTotal the recordsTotal to set
     */
    public void setRecordsTotal(String recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    /**
     * Get list data
     *
     * @return
     */
    public List<T> getData() {
        return data;
    }

    /**
     * Set list data
     *
     * @param data
     */
    public void setData(List<T> data) {
        this.data = data;
    }


    /**
     * @return the first
     */
    public String getFirst() {
        return first;
    }


    /**
     * @param first the first to set
     */
    public void setFirst(String first) {
        this.first = first;
    }


    /**
     * @return the headerConfig
     */
    public List<T> getHeaderConfig() {
        return headerConfig;
    }


    /**
     * @param headerConfig the headerConfig to set
     */
    public void setHeaderConfig(List<T> headerConfig) {
        this.headerConfig = headerConfig;
    }
}
