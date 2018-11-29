package com.davidparry.jredisearch.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class Line {

    private String id;
    private BookParser.State state;
    private String description;
    private String verse;
    private String text;
    private String book;
    private String chapter;
    private int chapterIndex;

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        return new Builder(this);
    }

    public Line(Builder builder) {
        this.state = builder.state;
        this.description = builder.description;
        this.verse = builder.verse;
        this.text = builder.text;
        this.book = builder.book;
        this.chapter = builder.chapter;
        this.id = builder.id;
        this.chapterIndex = builder.chapterIndex;
    }

    public String getId() {
        return id;
    }

    public BookParser.State getState() {
        return state;
    }

    public String getDescription() {
        return description;
    }

    public String getVerse() {
        return verse;
    }

    public String getText() {
        return text;
    }

    public String getBook() {
        return book;
    }

    public String getChapter() {
        return chapter;
    }

    public int getChapterIndex() {
        return chapterIndex;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Line{");
        sb.append("id='").append(id).append('\'');
        sb.append(", state=").append(state);
        sb.append(", description='").append(description).append('\'');
        sb.append(", verse='").append(verse).append('\'');
        sb.append(", text='").append(text).append('\'');
        sb.append(", book='").append(book).append('\'');
        sb.append(", chapter='").append(chapter).append('\'');
        sb.append(", chapterIndex=").append(chapterIndex);
        sb.append('}');
        return sb.toString();
    }

    public static final class Builder {
        private BookParser.State state;
        private String description;
        private String verse;
        private String text;
        private String book;
        private String chapter;
        private String id;
        private int chapterIndex;

        public Builder() {
        }

        private Builder(Line line) {
            this.state = line.state;
            this.description = line.description;
            this.verse = line.verse;
            this.text = line.text;
            this.book = line.book;
            this.chapter = line.chapter;
            this.id = line.id;
            this.chapterIndex = line.chapterIndex;
        }

        public Builder append(String line) {
            if (StringUtils.isNotBlank(this.text)) {
                this.text = this.text + line;
            } else if (StringUtils.isNotBlank(line)) {
                this.text = line;
            }
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder state(BookParser.State state) {
            this.state = state;
            return this;
        }

        public Builder verse(String verse) {
            this.verse = verse;
            return this;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder book(String book) {
            this.book = book;
            return this;
        }

        public Builder chapter(String chapter) {
            this.chapter = chapter;
            return this;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder chapterIndex(int chapterIndex) {
            this.chapterIndex = chapterIndex;
            return this;
        }

        public Line build() {
            if (this.state == null) {
                this.state = BookParser.State.BLANK;
            }
            this.id = StringUtils.replaceAll(book, " ", "-")
                    + ":" + StringUtils.replaceAll(chapter, " ", "-") + ":" + verse;

            if(this.verse != null) {
                String[] chapter = this.verse.split(":");
                if (chapter != null && chapter.length > 0 && chapter[0] != null) {
                    if (StringUtils.isNumeric(chapter[0])) {
                        try {
                            this.chapterIndex = Integer.valueOf(chapter[0]).intValue();
                        } catch (Exception er) {
                            er.printStackTrace();
                        }
                    }
                }
            }

            return new Line(this);
        }
    }

}
