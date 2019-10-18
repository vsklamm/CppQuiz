package com.vsklamm.cppquiz.utils;

import androidx.annotation.NonNull;

public class DeepLinksUtils {

    private final String link;

    private boolean questionLinkParsed;

    private int questionId;

    public DeepLinksUtils(@NonNull final String link) {
        this.link = link;
    }

    private void ensureQuestionParse() {
        if (!questionLinkParsed) {
            throw new IllegalStateException("No question link found so far");
        }
    }

    public boolean isQuestionLink() {
        try {
            questionId = Integer.parseInt(link.substring(link.lastIndexOf("/") + 1)); // TODO: more smart parse
            questionLinkParsed = true;
        } catch (NumberFormatException ignored) {
            questionLinkParsed = false;
            return false;
        }
        return true;
    }

    public int getQuestionId() {
        ensureQuestionParse();
        return questionId;
    }

}
