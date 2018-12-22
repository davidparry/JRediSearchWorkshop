package com.davidparry.model;

import java.util.List;

public class AjaxResults {

    long total;
    List<AjaxResult> list;

    public AjaxResults(long total, List<AjaxResult> list) {
        this.total = total;
        this.list = list;
    }

    public long getTotal() {
        return total;
    }

    public List<AjaxResult> getList() {
        return list;
    }
}
