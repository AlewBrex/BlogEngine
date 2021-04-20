package main.api.response.result;

import lombok.Data;
import main.api.response.ResultResponse;

import java.util.HashMap;
import java.util.Map;

@Data
public class BadResultResponse implements ResultResponse {

  private Boolean result;
  private Map<String, String> errors = new HashMap<>();

  public BadResultResponse(Map<String, String> errors) {
    this.result = false;
    this.errors = errors;
  }

  public BadResultResponse(String object, String message) {
    this.result = false;
    errors.put(object, message);
  }

  public BadResultResponse() {
    this.result = false;
  }

  public void addError(String name, String description) {
    errors.put(name, description);
  }

  public Boolean hasErrors() {
    return getErrors().size() > 0;
  }
}
