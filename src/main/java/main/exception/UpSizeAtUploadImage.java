package main.exception;

public class UpSizeAtUploadImage extends RuntimeException {
  public UpSizeAtUploadImage(String message) {
    super(message);
  }
}
