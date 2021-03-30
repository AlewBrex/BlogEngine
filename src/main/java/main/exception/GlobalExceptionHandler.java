package main.exception;

import lombok.extern.log4j.Log4j2;
import main.Main;
import main.api.response.ResultResponse;
import main.api.response.result.FalseResultResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

@Log4j2
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  private static final Logger LOGGER = LogManager.getLogger(Main.class);

  @ExceptionHandler(value = {LoginUserWrongCredentialsException.class, IllegalStateException.class})
  protected ResponseEntity<ResultResponse> handleWrongCredentials(
      LoginUserWrongCredentialsException ex, WebRequest request) {
    LOGGER.info(ex.getMessage());
    return ResponseEntity.ok(new FalseResultResponse());
  }

  @ExceptionHandler(value = {WrongParameterException.class})
  protected ResponseEntity<ResultResponse> handleBlockStatistic(
      WrongParameterException ex, WebRequest request) {
    LOGGER.info(ex.getMessage());
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(value = {EmptyTextComment.class})
  protected ResponseEntity<ResultResponse> handleSendComment(
      EmptyTextComment ex, WebRequest request) {
    LOGGER.info(ex.getMessage());
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = {NotPresentPost.class})
  protected ResponseEntity<ResultResponse> handleFindPostById(
      NotPresentPost ex, WebRequest request) {
    LOGGER.info(ex.getMessage());
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(value = {UpSizeAtUploadImage.class})
  protected ResponseEntity<ResultResponse> handleUploadImage(
      UpSizeAtUploadImage ex, WebRequest request) {
    LOGGER.info(ex.getMessage());
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  protected ResponseEntity<Object> handleExceptionInternal(
      Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
    if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
      request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
    }
    return super.handleExceptionInternal(ex, body, headers, status, request);
  }
}
