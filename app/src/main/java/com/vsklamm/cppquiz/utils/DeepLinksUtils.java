package com.vsklamm.cppquiz.utils;

import android.support.annotation.NonNull;

public class DeepLinksUtils {

    private final String link;

    private boolean questionLinkParsed;

    private boolean quizLinkParsed;

    private int questionId;

    private String quizKey;

    public DeepLinksUtils(@NonNull final String link) {
        this.link = link;
    }

    private void ensureQuestionParse() {
        if (!questionLinkParsed) {
            throw new IllegalStateException("No question link found so far");
        }
    }

    private void ensureQuizParse() {
        if (!quizLinkParsed) {
            throw new IllegalStateException("No quiz link found so far");
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

    public boolean isQuizLink() {
        try {
            quizKey = link.substring(link.lastIndexOf("/") + 1); // TODO: more smart parse
            questionLinkParsed = quizKey.length() == 5;
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

    public String getQuizKey() {
        isQuizLink();
        return quizKey;
    }

}
