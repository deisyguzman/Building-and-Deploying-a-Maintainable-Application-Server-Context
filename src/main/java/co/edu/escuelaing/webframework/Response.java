package co.edu.escuelaing.webframework;

public final class Response {
    private int statusCode = 200;
    private String contentType = "text/plain; charset=utf-8";

    public int getStatusCode() {
        return statusCode;
    }

    public String getContentType() {
        return contentType;
    }

    public void status(int statusCode) {
        this.statusCode = statusCode;
    }

    public void contentType(String contentType) {
        this.contentType = contentType;
    }
}
