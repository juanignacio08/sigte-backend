package unsch.fcsa.sigte.application.command;

import java.util.List;

public class PageResult<T> {

    private List<T> content;
    private final int page;
    private final int size;
    private final long totalElements;
    private final int totalPages;
    private final boolean isFirst;
    private final boolean isLast;

    public PageResult(int page, int size, long totalElements, int totalPages, boolean isFirst, boolean isLast, List<T> content) {
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.isFirst = isFirst;
        this.isLast = isLast;
        this.content = content;
    }

    public List<T> getContent() {
        return content;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }
}
