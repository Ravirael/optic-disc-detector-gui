package pl.polsl.detector;

/**
 * Created by rames on 14.04.17.
 */
public class FailedDetectionException extends Exception{
    FailedDetectionException(Throwable cause) {
        super(cause.getMessage(), cause);
    }

    FailedDetectionException(String message, Throwable cause) {
        super(message, cause);
    }

    FailedDetectionException(String message) {
        super(message);
    }
}
