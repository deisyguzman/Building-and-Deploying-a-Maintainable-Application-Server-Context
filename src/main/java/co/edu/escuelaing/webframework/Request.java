package co.edu.escuelaing.webframework;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class Request {
    private final String method;
    private final String path;
    private final Map<String, String> queryParameters;

    public Request(String method, String target) {
        this.method = method;
        int queryStart = target.indexOf('?');
        this.path = queryStart < 0 ? target : target.substring(0, queryStart);
        this.queryParameters = parseQuery(queryStart < 0 ? "" : target.substring(queryStart + 1));
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getValue(String name) {
        return queryParameters.get(name);
    }

    public Map<String, String> getQueryParameters() {
        return Collections.unmodifiableMap(queryParameters);
    }

    private static Map<String, String> parseQuery(String query) {
        Map<String, String> values = new LinkedHashMap<>();
        if (query.isBlank()) {
            return values;
        }
        for (String pair : query.split("&")) {
            if (pair.isBlank()) {
                continue;
            }
            int separator = pair.indexOf('=');
            String key = separator < 0 ? pair : pair.substring(0, separator);
            String value = separator < 0 ? "" : pair.substring(separator + 1);
            values.put(decode(key), decode(value));
        }
        return values;
    }

    private static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}
