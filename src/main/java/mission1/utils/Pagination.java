package mission1.utils;

import java.util.List;

public class Pagination<T> {
    private final List<T> items;
    private final int page;
    private final int pageSize;

    public Pagination(List<T> items, int page, int pageSize) {
        this.items = items;
        this.page = Math.max(page, 1);
        this.pageSize = pageSize;
    }

    public List<T> getPageItems() {
        int total = items.size();
        if (total == 0) return items;

        int from = (page - 1) * pageSize;
        int to = Math.min(from + pageSize, total);
        return items.subList(from, to);
    }

    public int getTotalPages() {
        int total = items.size();
        return total == 0 ? 1 : (int) Math.ceil(total / (double) pageSize);
    }

    public int getPage() {
        return page;
    }
}
