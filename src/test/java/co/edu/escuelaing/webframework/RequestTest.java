package co.edu.escuelaing.webframework;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class RequestTest {
    @Test
    void extractsMultipleDecodedQueryValues() {
        Request request = new Request("GET", "/hello?name=Pedro%20Gomez&language=en");

        assertEquals("/hello", request.getPath());
        assertEquals("Pedro Gomez", request.getValue("name"));
        assertEquals("en", request.getValue("language"));
        assertNull(request.getValue("missing"));
    }
}
