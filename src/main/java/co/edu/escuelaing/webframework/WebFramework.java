package co.edu.escuelaing.webframework;

import java.io.IOException;

public final class WebFramework {
    private static final Router ROUTER = new Router();
    private static final StaticFileService STATIC_FILES = new StaticFileService();

    private WebFramework() {
    }

    public static void staticfiles(String path) {
        STATIC_FILES.setRoot(path);
    }

    public static void get(String path, Service service) {
        ROUTER.get(path, service);
    }

    public static void start() throws IOException {
        start(readPort());
    }

    public static void start(int port) throws IOException {
        HttpServer.start(port, ROUTER, STATIC_FILES);
    }

    public static void stop() {
        HttpServer.stop();
    }

    private static int readPort() {
        String value = System.getenv("PORT");
        if (value == null || value.isBlank()) {
            return 8080;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            return 8080;
        }
    }
}
