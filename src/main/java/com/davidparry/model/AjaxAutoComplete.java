package com.davidparry.model;

public class AjaxAutoComplete {
    String value;
    String data;

    public AjaxAutoComplete(String value, String data) {
        this.value = value;
        this.data = data;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
