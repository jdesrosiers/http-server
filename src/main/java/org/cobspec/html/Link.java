package org.cobspec.html;

public class Link {
    private String href;
    private String display;

    public Link(String href, String display) {
        this.href = href;
        this.display = display;
    }

    public String getHref() {
        return href;
    }

    public String getDisplay() {
        return display;
    }
}
