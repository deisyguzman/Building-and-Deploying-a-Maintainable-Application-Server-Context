package co.edu.escuelaing.app;

import static co.edu.escuelaing.webframework.WebFramework.get;
import static co.edu.escuelaing.webframework.WebFramework.start;
import static co.edu.escuelaing.webframework.WebFramework.stop;
import static co.edu.escuelaing.webframework.WebFramework.staticfiles;

public final class Application {
    private Application() {
    }

    public static void main(String[] args) throws Exception {
        staticfiles(System.getenv().getOrDefault("STATIC_FILES_PATH", "/webroot"));
        String greetingPrefix = System.getenv().getOrDefault("GREETING_PREFIX", "Hello");

        get("/hello", (request, response) -> {
            String name = request.getValue("name");
            if (name == null || name.isBlank()) {
                name = "world";
            }
            return greetingPrefix + " " + name;
        });
        get("/pi", (request, response) -> String.valueOf(Math.PI));
        get("/health", (request, response) -> "OK");

        String environment = System.getenv().getOrDefault("APP_ENV", "development");
        if ("development".equalsIgnoreCase(environment)) {
            get("/shutdown", (request, response) -> {
                stop();
                return "Server will stop after this response.";
            });
        }
        start();
    }
}
