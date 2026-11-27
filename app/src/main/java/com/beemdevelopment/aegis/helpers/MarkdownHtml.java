package com.beemdevelopment.aegis.helpers;

public final class MarkdownHtml {
    private MarkdownHtml() {
    }

    public static String render(String markdown, String backgroundColor, String textColor) {
        String body = toHtml(markdown);
        return "<html><head><style type=\"text/css\">"
                + "body{background-color:" + backgroundColor + ";color:" + textColor + ";font-size:0.95em;line-height:1.4;}"
                + "a{color:" + textColor + ";}"
                + "table{border-collapse:collapse;width:100%;}"
                + "td,th{border:1px solid " + textColor + ";padding:4px;vertical-align:top;text-align:left;}"
                + "</style></head><body>" + body + "</body></html>";
    }

    static String toHtml(String markdown) {
        String[] lines = markdown.replace("\r\n", "\n").split("\n", -1);
        StringBuilder out = new StringBuilder();
        StringBuilder paragraph = new StringBuilder();
        boolean inList = false;
        boolean inTable = false;

        for (String raw : lines) {
            String line = raw.trim();
            if (line.startsWith("|") && line.endsWith("|") && line.contains("---")) {
                continue;
            }
            if (line.startsWith("|") && line.endsWith("|")) {
                flushParagraph(out, paragraph);
                if (!inTable) {
                    closeList(out, inList);
                    inList = false;
                    out.append("<table>");
                    inTable = true;
                }
                out.append("<tr>");
                String[] cells = line.substring(1, line.length() - 1).split("\\|", -1);
                for (String cell : cells) {
                    out.append("<td>").append(inline(cell.trim())).append("</td>");
                }
                out.append("</tr>");
                continue;
            }
            if (inTable) {
                out.append("</table>");
                inTable = false;
            }
            if (line.isEmpty()) {
                flushParagraph(out, paragraph);
                closeList(out, inList);
                inList = false;
                continue;
            }
            if (line.startsWith("### ")) {
                flushParagraph(out, paragraph);
                closeList(out, inList);
                inList = false;
                out.append("<h3>").append(inline(line.substring(4))).append("</h3>");
            } else if (line.startsWith("## ")) {
                flushParagraph(out, paragraph);
                closeList(out, inList);
                inList = false;
                out.append("<h2>").append(inline(line.substring(3))).append("</h2>");
            } else if (line.startsWith("# ")) {
                flushParagraph(out, paragraph);
                closeList(out, inList);
                inList = false;
                out.append("<h1>").append(inline(line.substring(2))).append("</h1>");
            } else if (line.startsWith("- ")) {
                flushParagraph(out, paragraph);
                if (!inList) {
                    out.append("<ul>");
                    inList = true;
                }
                out.append("<li>").append(inline(line.substring(2))).append("</li>");
            } else {
                closeList(out, inList);
                inList = false;
                if (paragraph.length() > 0) {
                    paragraph.append(' ');
                }
                paragraph.append(line);
            }
        }
        flushParagraph(out, paragraph);
        closeList(out, inList);
        if (inTable) {
            out.append("</table>");
        }
        return out.toString();
    }

    private static void flushParagraph(StringBuilder out, StringBuilder paragraph) {
        if (paragraph.length() == 0) {
            return;
        }
        out.append("<p>").append(inline(paragraph.toString())).append("</p>");
        paragraph.setLength(0);
    }

    private static void closeList(StringBuilder out, boolean inList) {
        if (inList) {
            out.append("</ul>");
        }
    }

    private static String inline(String text) {
        String escaped = text.replace("&", "&").replace("<", "<").replace(">", ">");
        StringBuilder out = new StringBuilder();
        int i = 0;
        while (i < escaped.length()) {
            if (escaped.startsWith("**", i)) {
                int end = escaped.indexOf("**", i + 2);
                if (end > i) {
                    out.append("<strong>").append(escaped, i + 2, end).append("</strong>");
                    i = end + 2;
                    continue;
                }
            }
            if (escaped.charAt(i) == '[') {
                int labelEnd = escaped.indexOf("](", i);
                int urlEnd = labelEnd >= 0 ? escaped.indexOf(')', labelEnd + 2) : -1;
                if (labelEnd > i && urlEnd > labelEnd) {
                    String label = escaped.substring(i + 1, labelEnd);
                    String url = escaped.substring(labelEnd + 2, urlEnd);
                    if (url.startsWith("https://") || url.startsWith("http://")) {
                        out.append("<a href=\"").append(url).append("\">").append(label).append("</a>");
                    } else {
                        out.append(label);
                    }
                    i = urlEnd + 1;
                    continue;
                }
            }
            out.append(escaped.charAt(i));
            i++;
        }
        return out.toString();
    }
}
