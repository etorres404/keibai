package io.github.keibai.models.meta;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BodyWS {
    public String type;
    public String nonce;
    public String json;

    public static BodyWS fromString(String text) {
        Pattern pattern = Pattern.compile("(.*?),(.*?),(.*)");
        Matcher matcher = pattern.matcher(text);

        if (!matcher.matches()) {
            return new BodyWS();
        }

        BodyWS body = new BodyWS();
        body.type = matcher.group(1);
        body.nonce = matcher.group(2);
        body.json = matcher.group(3);

        return body;
    }

    public boolean isEmpty() {
        return type == null || type.equals("")
                && nonce == null || nonce.equals("")
                && json == null || json.equals("");
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s", type, nonce, json);
    }
}