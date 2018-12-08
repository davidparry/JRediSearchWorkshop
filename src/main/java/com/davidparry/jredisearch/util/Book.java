package com.davidparry.jredisearch.util;

import java.util.Set;

public enum Book {

    INSTANCE;
    Set<Line> lines;

    public Set<Line> getLines() {
        if (lines == null) {
            lines = new BookParser().parse("book");
        }
        return lines;
    }

}
