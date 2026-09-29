package co.edu.escuelaing.webframework;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public final class HttpServer {
    private static volatile boolean running;

    private HttpServer() {
    }

    public static void start(int port, Router router, StaticFileService staticFiles) throws IOException {
        running = true;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server running on port " + port);
            while (running) {
                try (Socket client = serverSocket.accept()) {
                    handleRequest(client, router, staticFiles);
                } catch (IOException exception) {
                    if (running) {
                        System.err.println("Request error: " + exception.getMessage());
                    }
                }
            }
        } finally {
            running = false;
        }
        System.out.println("Server stopped gracefully.");
    }

    public static void stop() {
        running = false;
    }

    private static void handleRequest(Socket client, Router router, StaticFileService staticFiles) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.US_ASCII));
        String requestLine = reader.readLine();
        if (requestLine == null || requestLine.isBlank()) {
            writeText(client.getOutputStream(), 400, "Bad Request");
            return;
        }
        String[] parts = requestLine.split(" ");
        if (parts.length < 2) {
            writeText(client.getOutputStream(), 400, "Bad Request");
            return;
        }
        Request request = new Request(parts[0], parts[1]);
        Response response = new Response();
        Optional<Service> route = router.find(request.getMethod(), request.getPath());
        if (route.isPresent()) {
            String body = route.get().handle(request, response);
            write(client.getOutputStream(), response.getStatusCode(), response.getContentType(), body.getBytes(StandardCharsets.UTF_8));
            return;
        }
        Optional<StaticFileService.StaticResource> resource = staticFiles.find(request.getPath());
        if (resource.isPresent()) {
            StaticFileService.StaticResource file = resource.get();
            write(client.getOutputStream(), 200, file.contentType(), file.content());
            return;
        }
        writeText(client.getOutputStream(), 404, "404 Not Found");
    }

    private static void writeText(OutputStream output, int status, String body) throws IOException {
        write(output, status, "text/plain; charset=utf-8", body.getBytes(StandardCharsets.UTF_8));
    }

    private static void write(OutputStream output, int status, String contentType, byte[] body) throws IOException {
        String statusText = status == 200 ? "OK" : status == 400 ? "Bad Request" : "Not Found";
        String headers = "HTTP/1.1 " + status + " " + statusText + "\r\n"
                + "Content-Type: " + contentType + "\r\n"
                + "Content-Length: " + body.length + "\r\n"
                + "Connection: close\r\n\r\n";
        output.write(headers.getBytes(StandardCharsets.US_ASCII));
        output.write(body);
        output.flush();
    }
}
