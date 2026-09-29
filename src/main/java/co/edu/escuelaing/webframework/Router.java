package co.edu.escuelaing.webframework;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class Router {
    private final Map<String, Service> getRoutes = new HashMap<>();

    public void get(String path, Service service) {
        getRoutes.put(path, service);
    }

    public Optional<Service> find(String method, String path) {
        if (!"GET".equalsIgnoreCase(method)) {
            return Optional.empty();
        }
        return Optional.ofNullable(getRoutes.get(path));
    }
}
