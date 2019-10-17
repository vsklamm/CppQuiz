package com.pddstudio.highlightjs.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * This Class was created by Patrick J
 * on 09.06.16. For more Details and Licensing
 * have a look at the README.md
 */

public class SourceUtils {

	public static String generateContent(final String source, @NonNull final String style, @Nullable final String language, final boolean supportZoom, final boolean showLineNumbers) {
		return getStylePageHeader(supportZoom).append(
				getSourceForStyle(style)).append(
				(showLineNumbers ? getLineNumberStyling() : "")).append(
				getScriptPageHeader(showLineNumbers)).append(
				getSourceForLanguage(source, language)).append(
				getTemplateFooter()).toString();
	}

	private static StringBuffer getStylePageHeader(final boolean enableZoom) {
		return new StringBuffer("<!DOCTYPE html>\n")
				.append("<html>\n")
				.append("<head>\n")
				.append("    <meta charset=\"utf-8\">\n")
				.append((enableZoom ? "" : "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0\">\n"))
				.append("    <style type=\"text/css\">\n")
				.append("       html, body {\n")
				.append("           width:100%;\n")
				.append("           height: 100%;\n")
				.append("           margin: 0px;\n")
				.append("           padding: 0px;\n")
				.append("       }\n")
				.append("   </style>\n");
	}

	private static StringBuffer getScriptPageHeader(final boolean showLineNumbers) {
		return new StringBuffer("    <script src=\"./highlight.pack.js\"></script>\n")
				.append((showLineNumbers ? "<script src=\"./highlightjs-line-numbers.min.js\"></script>\n" : ""))
				.append("    <script>hljs.initHighlightingOnLoad();</script>\n")
				.append((showLineNumbers ? "<script>hljs.initLineNumbersOnLoad();</script>\n" : ""))
				.append("</head>\n")
				.append("<body style=\"margin: 0; padding: 0\" class=\"hljs\">\n");
	}

	private static StringBuffer getLineNumberStyling() {
		return new StringBuffer("<style type=\"text/css\">\n")
				.append(".hljs-line-numbers {\n")
				.append("\ttext-align: right;\n")
				.append("\tborder-right: 1px solid #ccc;\n")
				.append("\tcolor: #999;\n")
				.append("\t-webkit-touch-callout: none;\n")
				.append("\t-webkit-user-select: none;\n")
				.append("\t-khtml-user-select: none;\n")
				.append("\t-moz-user-select: none;\n")
				.append("\t-ms-user-select: none;\n")
				.append("\tuser-select: none;\n")
				.append("}\n")
				.append("</style>\n");
	}

	private static StringBuffer getTemplateFooter() {
		return new StringBuffer("</body>\n</html>\n");
	}

	private static String formatCode(String code) {
		return code.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}

	private static String getSourceForStyle(final String style) {
		return String.format("<link rel=\"stylesheet\" href=\"./styles/%s.css\">\n", style);
	}

	private static StringBuffer getSourceForLanguage(final String source, final String language) {
		if (language != null) { return new StringBuffer(String.format("<pre><code class=\"%s\">%s</code></pre>\n", language, formatCode(source))); } else {
			return new StringBuffer(String.format("<pre><code>%s</code></pre>\n", formatCode(source)));
		}
	}

}
