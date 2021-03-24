package main.exception;

public class WrongParameterException extends RuntimeException {
  public WrongParameterException() {
    super();
  }

  public WrongParameterException(String message) {
    super(message);
  }
}