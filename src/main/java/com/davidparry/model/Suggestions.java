package com.davidparry.model;

import java.util.ArrayList;
import java.util.List;

public class Suggestions {
    private List<AjaxAutoComplete> suggestions = new ArrayList();

    public Suggestions(List<AjaxAutoComplete> suggestions) {
        this.suggestions = suggestions;
    }

    public List<AjaxAutoComplete> getSuggestions() {
        return suggestions;
    }
}
