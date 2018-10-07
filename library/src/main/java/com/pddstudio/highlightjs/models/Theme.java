package com.pddstudio.highlightjs.models;

import java.util.Comparator;

/**
 * This Class was created by Patrick J
 * on 09.06.16. For more Details and Licensing
 * have a look at the README.md
 */

public enum Theme {
    AGATE("agate", true, false),
    ANDROID_STUDIO("androidstudio", true, true),
    ARDUINO_LIGHT("arduino-light", true, false),
    ARTA("arta", true, false),
    ASCETIC("ascetic", true, false),
    ATELIER_CAVE_DARK("atelier-cave-dark", true, false),
    ATELIER_CAVE_LIGHT("atelier-cave-light", true, false),
    ATELIER_DUNE_DARK("atelier-dune-dark", true, false),
    ATELIER_DUNE_LIGHT("atelier-dune-light", true, false),
    ATELIER_ESTUARY_DARK("atelier-estuary-dark", true, false),
    ATELIER_ESTUARY_LIGHT("atelier-estuary-light", true, false),
    ATELIER_FOREST_DARK("atelier-forest-dark", true, false),
    ATELIER_FOREST_LIGHT("atelier-forest-light", true, false),
    ATELIER_HEATH_DARK("atelier-heath-dark", true, false),
    ATELIER_HEATH_LIGHT("atelier-heath-light", true, false),
    ATELIER_LAKESIDE_DARK("atelier-lakeside-dark", true, false),
    ATELIER_LAKESIDE_LIGHT("atelier-lakeside-light", true, false),
    ATELIER_PLATEAU_DARK("atelier-plateau-dark", true, false),
    ATELIER_PLATEAU_LIGHT("atelier-plateau-light", true, false),
    ATELIER_SAVANNA_DARK("atelier-savanna-dark", true, false),
    ATELIER_SAVANNA_LIGHT("atelier-savanna-light", true, false),
    ATELIER_SEASIDE_DARK("atelier-seaside-dark", true, false),
    ATELIER_SEASIDE_LIGHT("atelier-seaside-light", true, false),
    ATELIER_SULPHURPOOL_DARK("atelier-sulphurpool-dark", true, false),
    ATELIER_SULPHURPOOL_LIGHT("atelier-sulphurpool-light", true, false),
    ATOM_ONE_DARK("atom-one-dark", true, false),
    ATOM_ONE_LIGHT("atom-one-light", true, false),
    BROWN_PAPER("brown-paper", true, false),
    CODEPEN_EMBED("codepen-embed", true, false),
    COLOR_BREWER("color-brewer", true, false),
    DARK("dark", true, false),
    DARKULA("darkula", true, true),
    DEFAULT("default", true, false),
    DOCCO("docco", true, false),
    DRAKULA("drakula", true, false),
    FAR("far", true, true),
    FOUNDATION("foundaiton", true, false),
    GITHUB("github", true, true),
    GITHUB_GIST("github-gist", true, false),
    GOOGLECODE("googlecode", true, false),
    GRAYSCALE("grayscale", true, false),
    GRUVBOX_DARK("gruvbox-dark", true, false),
    GRUVBOX_LIGHT("gruvbox-light", true, false),
    HOPSCOTCH("hopscotch", true, false),
    HYBRID("hybrid", true, false),
    IDEA("idea", true, true),
    IR_BLACK("ir-black", true, false),
    KIMBIE_DARK("kimbie.dark", true, false),
    KIMBIE_LIGHT("kimbie.light", true, false),
    MAGULA("magula", true, false),
    MONO_BLUE("mono-blue", true, false),
    MONOKAI("monokai", true, false),
    MONOKAI_SUBLIME("monokai-sublime", true, false),
    OBSIDIAN("obsidian", true, false),
    OCEAN("ocean", true, false),
    PARAISO_DARK("paraiso-dark", true, false),
    PARAISO_LIGHT("paraiso-light", true, false),
    POJOAQUE("pojoaque", true, false),
    PURE_BASIC("purebasic", true, false),
    QT_CREATOR_DARK("qtcreator_dark", true, false),
    QT_CREATOR_LIGHT("qtcreator_light", true, true),
    RAILSCASTS("railscasts", true, false),
    RAINBOX("rainbow", true, false),
    ROUTEROS("routeros", true, false),
    SCHOOL_BOOK("school-book", true, false),
    SOLARIZED_DARK("solarized-dark", true, false),
    SOLARIZED_LIGHT("solarized-light", true, false),
    SUNBURST("sunburst", true, false),
    TOMORROW("tomorrow", true, false),
    TOMORROW_NIGHT("tomorrow-night", true, false),
    TOMORROW_NIGHT_BLUE("tomorrow-night-blue", true, false),
    TOMORROW_NIGHT_BRIGHT("tomorrow-night-bright", true, false),
    TOMORROW_NIGHT_EIGHTIES("tomorrow-night-eighties", true, false),
    VS("vs", true, true),
    VS2015("vs2015", true, true),
    X_CODE("xcode", true, false),
    XT256("xt256", true, false),
    ZENBURN("zenburn", true, false);

    private final String themeName;
    private final boolean popular;
    private final boolean dark;

    Theme(String themeName) {
        this(themeName, false, false);
    }

    Theme(String themeName, boolean isDark, boolean isPopular) {
        this.themeName = themeName;
        this.dark = isDark;
        this.popular = isPopular;
    }

    public String getName() {
        return themeName;
    }

    public boolean isPopular() {
        return popular;
    }

    public boolean isDark() {
        return dark;
    }

}
