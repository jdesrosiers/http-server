package org.flint.formurlencoded;

import javaslang.Tuple2;
import javaslang.collection.List;
import javaslang.collection.HashMap;
import javaslang.collection.Map;
import javaslang.control.Option;

import static org.flint.parse.FormUrlencoded.PARSER;
import org.flint.parse.Uri;

public class FormUrlencoded {
    private HashMap<String, String> values;

    public FormUrlencoded() {
        values = HashMap.empty();
    }

    public FormUrlencoded(String content) {
        values = PARSER.parse(content);
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }

    public Map<String, String> toMap() {
        return values;
    }

    public Option<String> get(String key) {
        return values.get(key);
    }

    public void put(String key, String value) {
        values = values.put(key, value);
    }

    public List<Tuple2<String, String>> toList() {
        return values.toList();
    }

    private String entryToString(Tuple2<String, String> entry) {
        String str = Uri.ENCODE.parse(entry._1);
        if (entry._2 != null) {
            str += "=" + Uri.ENCODE.parse(entry._2);
        }

        return str;
    }

    public String toQueryString() {
        return values
            .map(this::entryToString)
            .intersperse("&")
            .fold("", String::concat);
    }
}
