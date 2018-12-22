package com.davidparry.example.util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * Very rough class to parse the file
 */
public class BookParser {

    public Set<Line> parse(String file) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream(file);
        LineIterator li;
        Set<Line> items = new HashSet<>();

        try {
            li = IOUtils.lineIterator(is, "utf-8");
            String title = "";
            int lineNumber = 1;
            int chapter =0;
            while (li.hasNext()) {
                String line = li.nextLine();
                Line l = inspect(line);
                if (State.CHAPTER.equals(l.getState())) {
                    chapter++;
                } else if (State.TITLE.equals(l.getState())) {
                    title = l.getTitle();
                } else if (State.LINE.equals(l.getState())) {
                    items.add(l.toBuilder().title(title).chapter(chapter).line(lineNumber).build());
                }
                lineNumber++;
            }
            li.close();
        } catch (Exception er) {
            er.printStackTrace();
        }

        return items;
    }

    private Line inspect(String line) {
        if (StringUtils.isAllBlank(line)) {
            return Line.builder().text(line).state(State.BLANK).build();
        }

        if (StringUtils.contains(line, "CHAP.")) {
            return Line.builder().state(State.CHAPTER).build();
        } else if (StringUtils.contains(line, "||")) {
            line = StringUtils.replaceAll(line, "\\||", "").trim();
            return Line.builder().title(line).state(State.TITLE).build();
        } else {
            return Line.builder().text(line.trim()).state(State.LINE).build();
        }
    }

    public enum State {
        BLANK, CHAPTER, TITLE, LINE
    }


}
