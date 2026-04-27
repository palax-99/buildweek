package finalBW.buildweek.exception;

public class CsvReadingProblemException extends RuntimeException {
    public CsvReadingProblemException(String message) {
        super(message);
    }

    public CsvReadingProblemException(String message, Throwable cause) {
        super(message, cause);
    }
}


