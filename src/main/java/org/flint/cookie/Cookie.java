package org.flint.cookie;

import javaslang.collection.HashMap;
import javaslang.control.Option;

import static org.flint.parse.Cookie.PARSER;

public class Cookie {
    private HashMap<String, String> cookie;

    public Cookie() {
        this.cookie = HashMap.empty();
    }

    public Cookie(String content) {
        this.cookie = PARSER.parse(content);
    }

    public boolean isEmpty() {
        return cookie.isEmpty();
    }

    public Option<String> get(String key) {
        return cookie.get(key);
    }

    public void put(String key, String value) {
        cookie = cookie.put(key, value);
    }

    @Override
    public String toString() {
        return cookie
            .map((entry) -> entry._1 + "=" + entry._2)
            .mkString("; ");
    }
}
