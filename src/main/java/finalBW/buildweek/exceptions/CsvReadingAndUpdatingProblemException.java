package finalBW.buildweek.exceptions;

public class CsvReadingAndUpdatingProblemException extends RuntimeException {
    public CsvReadingAndUpdatingProblemException(String message) {
        super(message);
    }

    public CsvReadingAndUpdatingProblemException(String message, Throwable cause) {
        super(message, cause);
    }
}


