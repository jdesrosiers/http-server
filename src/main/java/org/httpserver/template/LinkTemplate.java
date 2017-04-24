package org.httpserver.template;

import org.httpserver.html.Link;

public class LinkTemplate {
    private static String template = "<li><a href=\"/%s\">%s</a></li>";

    public static String render(Link link) {
        return String.format(template, link.getHref(), link.getDisplay());
    }
}
