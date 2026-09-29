package co.edu.escuelaing.webframework;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

public final class StaticFileService {
    private static final Map<String, String> CONTENT_TYPES = Map.of(
            ".html", "text/html; charset=utf-8",
            ".css", "text/css; charset=utf-8",
            ".js", "application/javascript; charset=utf-8",
            ".svg", "image/svg+xml",
            ".png", "image/png",
            ".jpg", "image/jpeg",
            ".jpeg", "image/jpeg",
            ".gif", "image/gif"
    );

    private String root = "/webroot";

    public void setRoot(String root) {
        this.root = root.startsWith("/") ? root : "/" + root;
    }

    public Optional<StaticResource> find(String requestPath) {
        String cleanPath = requestPath.startsWith("/") ? requestPath : "/" + requestPath;
        if (cleanPath.contains("..")) {
            return Optional.empty();
        }
        String resourceName = root + ("/".equals(cleanPath) ? "/index.html" : cleanPath);
        try (InputStream input = StaticFileService.class.getResourceAsStream(resourceName)) {
            if (input == null) {
                return Optional.empty();
            }
            String contentType = contentType(cleanPath);
            return Optional.of(new StaticResource(input.readAllBytes(), contentType));
        } catch (IOException exception) {
            return Optional.empty();
        }
    }

    private String contentType(String path) {
        int extensionStart = path.lastIndexOf('.');
        return extensionStart < 0
                ? "application/octet-stream"
                : CONTENT_TYPES.getOrDefault(path.substring(extensionStart).toLowerCase(), "application/octet-stream");
    }

    public record StaticResource(byte[] content, String contentType) {
    }
}
