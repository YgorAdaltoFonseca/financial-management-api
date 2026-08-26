package side.financialmanagementapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.net.URI;
import java.util.Date;

@ControllerAdvice
public class GlobalControllerAdvice {

    private static final Date timestamp = new Date();
    private static final String TIMESTAMP = "timestamp";

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    ProblemDetail exceptionHandler(MethodArgumentTypeMismatchException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());
        problemDetail.setType(URI.create(""));
        problemDetail.setTitle(HttpStatus.BAD_REQUEST.toString());
        problemDetail.setProperty(TIMESTAMP, timestamp.toString());
        return problemDetail;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ProblemDetail exceptionHandler(IllegalArgumentException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());
        problemDetail.setType(URI.create(""));
        problemDetail.setTitle(HttpStatus.BAD_REQUEST.toString());
        problemDetail.setProperty(TIMESTAMP, timestamp.toString());
        return problemDetail;
    }
}
