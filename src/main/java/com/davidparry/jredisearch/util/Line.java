package com.davidparry.jredisearch.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class Line {

    private String id;
    private BookParser.State state;
    private int line;
    private String text;
    private String title;
    private int chapter;

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        return new Builder(this);
    }

    public Line(Builder builder) {
        this.state = builder.state;
        this.line = builder.line;
        this.text = builder.text;
        this.title = builder.title;
        this.chapter = builder.chapter;
        this.id = builder.id;
    }

    public String getId() {
        return id;
    }

    public BookParser.State getState() {
        return state;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public int getChapter() {
        return chapter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(getId(), line.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }


    public static final class Builder {
        private BookParser.State state;
        private String description;
        private int line;
        private String text;
        private String title;
        private int chapter;
        private String id;

        public Builder() {
        }

        private Builder(Line line) {
            this.state = line.state;
            this.line = line.line;
            this.text = line.text;
            this.title = line.title;
            this.chapter = line.chapter;
            this.id = line.id;
        }

        public Builder append(String line) {
            if (StringUtils.isNotBlank(this.text)) {
                this.text = this.text + line;
            } else if (StringUtils.isNotBlank(line)) {
                this.text = line;
            }
            return this;
        }

        public Builder state(BookParser.State state) {
            this.state = state;
            return this;
        }

        public Builder line(int line) {
            this.line = line;
            return this;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder chapter(int chapter) {
            this.chapter = chapter;
            return this;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Line build() {
            if (this.state == null) {
                this.state = BookParser.State.BLANK;
            }
            this.id = chapter + ":" + line;
            return new Line(this);
        }
    }

}