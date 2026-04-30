package finalBW.buildweek.exceptions;

import java.util.List;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(List<String> messageList) {
        super(String.join(". ", messageList));
    }
}