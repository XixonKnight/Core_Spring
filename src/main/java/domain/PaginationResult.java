package domain;

import java.util.List;

/**
 * Created by Nhan Nguyen on 5/20/2021
 *
 * @author Nhan Nguyen
 * @date 5/20/2021
 */

//@Getter
//@Setter
public class PaginationResult<T> {
    private int totalRecords;
    private int currentPage;
    private List<T> data;
    private int maxResult;
    private int totalPages;
    private int first;

    public PaginationResult() {
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getMaxResult() {
        return maxResult;
    }

    public void setMaxResult(int maxResult) {
        this.maxResult = maxResult;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
