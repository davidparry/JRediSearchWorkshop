package com.davidparry.model;

public class AjaxResult {

    private String line;
    private String text;
    private String title;
    private String chapter;
    private double score;
    private String id;

    private AjaxResult(Builder builder) {
        line = builder.line;
        text = builder.text;
        title = builder.title;
        chapter = builder.chapter;
        score = builder.score;
        id = builder.id;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(AjaxResult copy) {
        Builder builder = new Builder();
        builder.line = copy.getLine();
        builder.text = copy.getText();
        builder.title = copy.getTitle();
        builder.chapter = copy.getChapter();
        builder.score = copy.getScore();
        builder.id = copy.getId();
        return builder;
    }

    public String getId() {
        return id;
    }

    public String getLine() {
        return line;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public String getChapter() {
        return chapter;
    }

    public double getScore() {
        return score;
    }

    /**
     * {@code AjaxResult} builder static inner class.
     */
    public static final class Builder {
        private String line;
        private String text;
        private String title;
        private String chapter;
        private double score;
        private String id;

        private Builder() {
        }

        /**
         * Sets the {@code line} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code line} to set
         * @return a reference to this Builder
         */
        public Builder withLine(String val) {
            line = val;
            return this;
        }

        /**
         * Sets the {@code text} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code text} to set
         * @return a reference to this Builder
         */
        public Builder withText(String val) {
            text = val;
            return this;
        }

        /**
         * Sets the {@code title} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code title} to set
         * @return a reference to this Builder
         */
        public Builder withTitle(String val) {
            title = val;
            return this;
        }

        /**
         * Sets the {@code chapter} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code chapter} to set
         * @return a reference to this Builder
         */
        public Builder withChapter(String val) {
            chapter = val;
            return this;
        }

        /**
         * Sets the {@code score} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code score} to set
         * @return a reference to this Builder
         */
        public Builder withScore(double val) {
            score = val;
            return this;
        }

        /**
         * Sets the {@code id} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code id} to set
         * @return a reference to this Builder
         */
        public Builder withId(String val) {
            id = val;
            return this;
        }

        /**
         * Returns a {@code AjaxResult} built from the parameters previously set.
         *
         * @return a {@code AjaxResult} built with parameters of this {@code AjaxResult.Builder}
         */
        public AjaxResult build() {
            return new AjaxResult(this);
        }
    }
}
