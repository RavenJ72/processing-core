package sasdevs.backend.exceptions;  // Тот же пакет

import java.util.List;

public class ErrorResponse {
    private String message;
    private int status;
    private List<String> details;

    public ErrorResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public ErrorResponse(String message, int status, List<String> details) {
        this.message = message;
        this.status = status;
        this.details = details;
    }

    public String getMessage() { return message; }
    public int getStatus() { return status; }
    public List<String> getDetails() { return details; }
}