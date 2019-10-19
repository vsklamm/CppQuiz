package com.pddstudio.highlightjs.models;

/**
 * This Class was created by Patrick J
 * on 09.06.16. For more Details and Licensing
 * have a look at the README.md
 */

public enum Language {
    AUTO_DETECT(null),
    C_PLUS_PLUS("cpp");

    private final String className;

    Language(String name) {
        this.className = name;
    }

    public String getName() {
        return className;
    }

}
