package com.epam.esm.dto;

import javax.validation.constraints.Positive;

public class PaginationParameter {

    @Positive
    private Integer page;
    @Positive
    private Integer size;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PaginationParameter{");
        sb.append("page=").append(page);
        sb.append(", size=").append(size);
        sb.append('}');
        return sb.toString();
    }
}
