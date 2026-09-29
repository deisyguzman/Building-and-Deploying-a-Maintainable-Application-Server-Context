package co.edu.escuelaing.webframework;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RouterTest {
    @Test
    void resolvesRegisteredGetRouteOnly() {
        Router router = new Router();
        router.get("/hello", (request, response) -> "Hello");

        assertTrue(router.find("GET", "/hello").isPresent());
        assertTrue(router.find("POST", "/hello").isEmpty());
        assertTrue(router.find("GET", "/unknown").isEmpty());
    }
}
