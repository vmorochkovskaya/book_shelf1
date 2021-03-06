package org.example.app.exception;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class FileExceptionAdvice extends ResponseEntityExceptionHandler {
    private final Logger logger = Logger.getLogger(FileExceptionAdvice.class);

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<Object> handleFileNotFoundException(FileNotFoundException exc) {
        logger.warn("File Not Found: " + exc.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File Not Found: " + exc.getMessage());
    }
}