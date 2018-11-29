package com.davidparry.jredisearch.util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Very rough class to parse the bible file
 */
public class BookParser {

    public Set<Line> parse(String file) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream(file);
        LineIterator li;
        Map<String, Line> verse = new HashMap<>();
        try {
            li = IOUtils.lineIterator(is, "utf-8");
            Line outcome = Line.builder().state(State.START).description("").build();
            String chapter = "";
            String book = "";

            while (li.hasNext()) {
                String line = li.nextLine();
                Line l = inspect(line, outcome);
                outcome = l;
                Line entry = null;
                if (StringUtils.isNotEmpty(l.getId())) {
                    entry = verse.remove(l.getId());
                }
                if (State.BOOK.equals(l.getState())) {
                    book = l.getText();
                } else if (State.CHAPTER.equals(l.getState())) {
                    chapter = l.getText();
                }

                if (entry != null) {
                    verse.put(entry.getId(), entry.toBuilder().book(book).chapter(chapter).append(l.getText()).build());
                } else {
                    verse.put(l.getId(), l.toBuilder().chapter(chapter).book(book).build());
                }
            }
            li.close();
        } catch (Exception er) {
            er.printStackTrace();
        }
        Set<Line> items = new HashSet<>();
        if (!verse.isEmpty()) {
            items.addAll(verse.values());
        }
        return items;
    }

    boolean checkLineText(String line) {
        char[] c = line.toCharArray();
        if (Character.isDigit(c[0]) || StringUtils.startsWith(line, "Chapter")) {
            return false;
        } else {
            return true;
        }
    }

    private Line inspect(String line, Line past) {
        if (StringUtils.isAllBlank(line)) {
            return Line.builder().text(line).build();
        }
        // we have a versus line
        char[] c = line.toCharArray();
        if (checkLineText(line) && past.getState().equals(State.VERSE)) {
            return past.toBuilder().append(" " + line).build();
        } else if (Character.isDigit(c[0])) {
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < c.length; i++) {
                char g = c[i];
                if (Character.isDigit(g)) {
                    buffer.append(g);
                } else if (':' == g) {
                    buffer.append(g);
                } else {
                    return Line.builder().state(State.VERSE).verse(buffer.toString()).text(buffer.toString()).build();
                }
            }
            return Line.builder().state(State.VERSE).text(buffer.toString()).build();
        } else if (checkLineText(line) && State.CHAPTER.equals(past.getState())) {
            return past.toBuilder().description(line).build();
        } else if (StringUtils.startsWith(line, "Chapter")) {
            return Line.builder().state(State.CHAPTER).text(line).build();
        } else if (State.BOOK.equals(past.getState())) {
            return past.toBuilder().description(line).build();
        } else {
            if (Character.isDigit(c[0])) {
                return Line.builder().state(State.VERSE).verse(line).build();
            } else {
                return Line.builder().state(State.BOOK).text(line).build();
            }
        }
    }

    public enum State {
        BLANK, CHAPTER, BOOK, VERSE, START
    }


    public static final List<Map> setDocMap(Set<Line> items, boolean addChapterIndex) {
        List<Map> maps = new ArrayList<>();
        for (Line doc : items) {
            if (doc.getState().equals(State.VERSE)) {
                Map<String, Object> fields = new HashMap<>();

                if (StringUtils.isNotBlank(doc.getText())) {
                    fields.put("text", doc.getText());
                } else {
                    fields.put("text", "empty");
                }
                if (StringUtils.isNotBlank(doc.getChapter())) {
                    fields.put("chapter", doc.getChapter());
                }
                if (StringUtils.isNotBlank(doc.getBook())) {
                    fields.put("book", doc.getBook());
                }
                if (StringUtils.isNotBlank(doc.getDescription())) {
                    fields.put("description", doc.getDescription());
                }
                if (StringUtils.isNotBlank(doc.getId())) {
                    fields.put("id", doc.getId());
                }
                if(addChapterIndex) {
                    fields.put("chapterIndex", doc.getChapterIndex());
                }

                if (StringUtils.isNotBlank(doc.getId())) {
                    maps.add(fields);
                }
            }
        }
        return maps;
    }

}
