package com.davidparry.model;

public class AjaxResponseBody {

    private String msg;
    private AjaxResults results;

    public AjaxResponseBody(String msg, AjaxResults results) {
        this.msg = msg;
        this.results = results;
    }

    public String getMsg() {
        return msg;
    }


    public AjaxResults getResults() {
        return results;
    }


}
