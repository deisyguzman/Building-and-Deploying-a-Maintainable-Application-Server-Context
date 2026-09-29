package co.edu.escuelaing.webframework;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StaticFileServiceTest {
    @Test
    void loadsHtmlAndRejectsUnknownOrUnsafeResources() {
        StaticFileService files = new StaticFileService();

        var index = files.find("/index.html");
        assertTrue(index.isPresent());
        assertEquals("text/html; charset=utf-8", index.get().contentType());
        assertTrue(new String(index.get().content()).contains("Sequential Service Desk"));
        assertTrue(files.find("/missing.txt").isEmpty());
        assertTrue(files.find("/../pom.xml").isEmpty());
    }
}
