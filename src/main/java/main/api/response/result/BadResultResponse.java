package main.api.response.result;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import main.api.response.ResultResponse;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
public class BadResultResponse implements ResultResponse {
    private boolean result;
    private Map<String, String> errors = new HashMap<>();

    public BadResultResponse(Map<String, String> errors) {
        this.result = false;
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void addError(String name, String description) {
        errors.put(name, description);
    }
}