package main.exception;

import lombok.extern.log4j.Log4j2;
import main.api.response.ResultResponse;
import main.api.response.result.BadResultResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.WebUtils;

import java.security.InvalidParameterException;

@Log4j2
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            UsernameNotFoundException.class,
            ContentNotAllowedException.class,
            InvalidParameterException.class})
    @Nullable
    public final ResponseEntity<ResultResponse> handleException(Exception ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();

        if (ex instanceof ContentNotAllowedException) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            ContentNotAllowedException cnae = (ContentNotAllowedException) ex;

            log.info(ex.getMessage());

            return handleContentNotAllowedException(cnae, headers, status, request);
        } else if (ex instanceof UsernameNotFoundException) {
            HttpStatus status = HttpStatus.UNAUTHORIZED;
            UsernameNotFoundException unfe = (UsernameNotFoundException) ex;

            log.info(ex.getMessage());

            return handleUsernameNotFoundException(unfe, headers, status, request);
        } else if (ex instanceof InvalidParameterException) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            InvalidParameterException ipe = (InvalidParameterException) ex;

            log.info(ex.getMessage());

            return handleInvalidParameterException(ipe, headers, status, request);
        } else {
            if (log.isWarnEnabled()) {
                log.warn("Unknown exception type: " + ex.getClass().getName());
            }
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            return handleExceptionInternal(ex, null, headers, status, request);
        }
    }

    protected ResponseEntity<ResultResponse> handleContentNotAllowedException(
            ContentNotAllowedException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        return handleExceptionInternal(ex, new BadResultResponse("content", ex.getMessage()), headers, status, request);
    }

    protected ResponseEntity<ResultResponse> handleUsernameNotFoundException(
            UsernameNotFoundException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        return handleExceptionInternal(ex, new BadResultResponse("user", ex.getMessage()), headers, status, request);
    }

    protected ResponseEntity<ResultResponse> handleInvalidParameterException(
            InvalidParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        return handleExceptionInternal(ex, new BadResultResponse("parameter", ex.getMessage()), headers, status, request);
    }

    protected ResponseEntity<ResultResponse> handleExceptionInternal(Exception ex, ResultResponse body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        }

        return new ResponseEntity<>(body, headers, status);
    }
}