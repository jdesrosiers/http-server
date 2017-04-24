package org.httpserver.template;

import javaslang.collection.List;

import org.httpserver.html.Index;

public class IndexTemplate {
    public static String render(Index index) {
        StringBuilder template = new StringBuilder();
        template.append("<html>");
        template.append("  <head>");
        template.append("    <title>Index - " + index.getDirectory() + "</title>");
        template.append("  </head>");
        template.append("  <body>");
        template.append("    <h1>Index - " + index.getDirectory() + "</h1>");
        template.append("    <ul>");
        index.getLinks()
            .map(LinkTemplate::render)
            .forEach(template::append);
        template.append("    </ul>");
        template.append("  </body>");
        template.append("</html>");

        return template.toString();
    }
}
