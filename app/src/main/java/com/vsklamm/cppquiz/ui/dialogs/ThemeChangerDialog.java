package com.vsklamm.cppquiz.ui.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Pair;

import com.pddstudio.highlightjs.models.Theme;
import com.vsklamm.cppquiz.R;

import java.util.ArrayList;

public class ThemeChangerDialog implements DialogInterface.OnClickListener {

    private final ThemeChangeListener changeListener;
    private final ArrayList<Pair<String, Theme>> styleArray;
    private AlertDialog dialog;

    public ThemeChangerDialog(@NonNull ThemeChangeListener themeChangeListener) {
        this.changeListener = themeChangeListener;
        this.styleArray = new ArrayList<>();
        initStyles();
    }

    private void initStyles() {
        for (Theme style : Theme.values()) {
            styleArray.add(new Pair<>(style.getName(), style));
        }
    }

    public void show(@NonNull Context context, final Theme currentTheme) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = null;
        buildDialog(context, currentTheme);
        dialog.show();
    }

    private void buildDialog(@NonNull Context context, final Theme currentTheme) {
        String[] themeNames = buildThemeList();
        int currentThemeId = 0;
        for (int i = 0; i != themeNames.length; ++i) {
            if (themeNames[i].equals(currentTheme.getName())) {
                currentThemeId = i;
                break;
            }
        }
        dialog = new AlertDialog.Builder(context)
                .setSingleChoiceItems(themeNames, currentThemeId, this)
                .setTitle(R.string.dialog_theme_selection_title)
                .create();
    }

    private String[] buildThemeList() {
        String[] temp = new String[styleArray.size()];
        int arrayIterator = 0;
        for (Pair<String, Theme> entry : styleArray) { // TODO: copy-paste
            if (entry.second.isPopular()) {
                temp[arrayIterator++] = entry.first;
            }
        }
        for (Pair<String, Theme> entry : styleArray) {
            if (!entry.second.isPopular()) {
                temp[arrayIterator++] = entry.first;
            }
        }
        return temp;
    }

    private Theme findThemeWithMatchingName(String themeName) {

        if (themeName == null || themeName.isEmpty()) {
            return Theme.DEFAULT;
        }

        for (Theme theme : Theme.values()) {
            if (theme.getName().equalsIgnoreCase(themeName)) {
                return theme;
            }
        }

        return Theme.DEFAULT;
    }

    private Theme getSelectedTheme(int selectedIndex) {

        CharSequence[] themes = buildThemeList();
        String themeName = null;

        if (selectedIndex > 0 && selectedIndex < themes.length) {
            themeName = themes[selectedIndex].toString();
        }

        return findThemeWithMatchingName(themeName);
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
        Theme newTheme = getSelectedTheme(i);
        changeListener.onChangeTheme(newTheme);
    }

    public interface ThemeChangeListener {
        void onChangeTheme(@NonNull Theme theme);
    }
}
