package co.edu.escuelaing.webframework;

@FunctionalInterface
public interface Service {
    String handle(Request request, Response response);
}
