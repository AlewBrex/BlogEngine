package main.api.response.result;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import main.api.response.ResultResponse;

import java.util.HashMap;
import java.util.Map;

@Data
@RequiredArgsConstructor
public class BadResultResponse implements ResultResponse {

    private boolean result;
    private Map<String, String> errors = new HashMap<>();

    public BadResultResponse(Map<String, String> errors) {
        this.result = false;
        this.errors = errors;
    }

    public BadResultResponse(String object, String message) {
        this.result = false;
        errors.put(object, message);
    }

    public void addError(String name, String description) {
        errors.put(name, description);
    }

    public Boolean hasErrors() {
        return getErrors().size() > 0;
    }
}