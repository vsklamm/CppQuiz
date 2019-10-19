package com.vsklamm.cppquiz.utils;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.functions.BiFunction;

public class StandardLinksUtils {

    final static String LOG = "StandardLinksUtils";

    public static String addLinks(String explanation) {
        final String possibleSectionName = "(\\[(\\w+(?:\\.\\w+)*)\\])?";
        final String sectionNumber = "§\\d+(?:\\.\\d+)*";
        final String possibleParagraph = "(?:¶(\\d+(?:\\.\\d+)*))*";
        Pattern patternLink = Pattern.compile("(" + possibleSectionName + sectionNumber + possibleParagraph + ")");

        final BiFunction<Matcher, Integer, String> getGroup = (m, g) -> {
            try {
                String res = m.group(g);
                return res == null ? "" : res;
            } catch (IndexOutOfBoundsException e) {
                return "";
            }
        };

        return StandardLinksUtils.replace(explanation, patternLink, m -> {
            String fullReference = "", sectionName = "", paragraphNumber = ""; // TODO: BAD SHIT
            try {
                fullReference = getGroup.apply(m, 0);
                sectionName = getGroup.apply(m, 3);
                paragraphNumber = getGroup.apply(m, 4);
            } catch (Exception e) {
                Log.e(LOG, e.getMessage() == null ? "Error while regexing links" : e.getMessage()); // TODO: bad shit
            }
            if (!sectionName.isEmpty()) {
                StringBuilder fullLink = new StringBuilder("https://timsong-cpp.github.io/cppwp/n4659/")
                        .append(sectionName);
                if (!paragraphNumber.isEmpty()) {
                    fullLink.append("#").append(paragraphNumber);
                }
                return "[_" + fullReference + "_](" + fullLink.toString() + ")";
            } else {
                return "_" + fullReference + "_";
            }
        });
    }

    private static String replace(final String input, Pattern regex, StringReplacerCallback callback) {
        StringBuffer resultString = new StringBuffer();
        Matcher regexMatcher = regex.matcher(input);
        while (regexMatcher.find()) {
            regexMatcher.appendReplacement(resultString, callback.replace(regexMatcher));
        }
        regexMatcher.appendTail(resultString);
        return resultString.toString();
    }

    public interface StringReplacerCallback {
        String replace(Matcher match);
    }

}
